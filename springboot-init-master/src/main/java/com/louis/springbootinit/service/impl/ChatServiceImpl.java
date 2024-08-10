package com.louis.springbootinit.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.dashscope.app.Application;
import com.alibaba.dashscope.app.ApplicationParam;
import com.alibaba.dashscope.app.ApplicationResult;
import com.alibaba.dashscope.app.RagApplicationParam;
import com.alibaba.dashscope.common.History;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.louis.springbootinit.common.ErrorCode;
import com.louis.springbootinit.constant.UserConstant;
import com.louis.springbootinit.job.GenerateQuestionsJob;
import com.louis.springbootinit.manager.AiManager;
import com.louis.springbootinit.exception.BusinessException;
import com.louis.springbootinit.mapper.HistoryMapper;
import com.louis.springbootinit.model.dto.BaiLianResult;
import com.louis.springbootinit.model.dto.request.ChatRequest;
import com.louis.springbootinit.model.dto.request.UserAsk;
import com.louis.springbootinit.model.entity.TaskInfo;
import com.louis.springbootinit.model.entity.User;
import com.louis.springbootinit.model.enums.ModelEnums;
import com.louis.springbootinit.service.ChatService;
import com.louis.springbootinit.service.HistoryService;
import com.louis.springbootinit.service.TaskInfoService;
import com.louis.springbootinit.utils.FileUtils;
import com.louis.springbootinit.utils.InciseStrUtils;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import javax.annotation.Generated;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.louis.springbootinit.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author louis
 * @version 1.0
 * @date 2024/5/25 22:40
 */
@Service
@Slf4j
public class ChatServiceImpl implements ChatService {
    // 默认调用政策大模型
    private static String appKey = ModelEnums.MODEL3.getAppKey();
    private static String apiKey = ModelEnums.MODEL3.getApiKey();
    private static final Map<String,ModelEnums> map = new HashMap<>();
    private static final int EXPIREDTIME = 3600000;
    @Value("${app.upload.dir}")
    private String uploadDir;
    @Value("${app.file.upload-pic}")
    private String uploadPic;
    @Value("${app.upload.prefix}")
    private String imagePrefix;
    @Resource
    private AiManager aiManager;
    @Resource
    private GenerateQuestionsJob job;
    @Resource
    private HistoryMapper historyMapper;
    @Resource
    private TaskInfoService taskService;
    static{
        map.put(ModelEnums.MODEL1.getModelId(),ModelEnums.MODEL1);
        map.put(ModelEnums.MODEL2.getModelId(),ModelEnums.MODEL2);
        map.put(ModelEnums.MODEL3.getModelId(),ModelEnums.MODEL3);
        map.put(ModelEnums.MODEL4.getModelId(),ModelEnums.MODEL4);
        map.put(ModelEnums.MODEL5.getModelId(),ModelEnums.MODEL5);
        map.put(ModelEnums.MODEL6.getModelId(),ModelEnums.MODEL6);
    }
    /**
     * 选择调用大模型
     * @param modelId
     */
    public void selectModel(String modelId){
        ModelEnums modelEnums = map.get(modelId);
        appKey = modelEnums.getAppKey();
        apiKey = modelEnums.getApiKey();
    }


    /**
     * 大模型文本调用接口（正式）
     * @param param
     * @param req
     * @param emitter
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SseEmitter sseChat(ChatRequest param, HttpServletRequest req, SseEmitter emitter) {
        User user = (User)req.getSession().getAttribute(USER_LOGIN_STATE);
        // 获取当前窗口所有对话信息
        ArrayDeque<UserAsk> his = param.getHistory();
        if(his.size() > 10){
            // 大于五轮对话（含用户和大模型）
            his.pollFirst();
        }
        selectModel(param.getModelId());
        QueryWrapper<com.louis.springbootinit.model.entity.History> historyQueryWrapper = new QueryWrapper<>();
        historyQueryWrapper.eq("userId",user.getId());
        historyQueryWrapper.eq("uuid",param.getUuid());
        Flowable<ApplicationResult> result = null;
        BaiLianResult bean = new BaiLianResult();
        boolean[] errorOccurred = {false};
        try {
            com.louis.springbootinit.model.entity.History target = historyMapper.selectOne(historyQueryWrapper);
            Date currentTime = new Date();
            boolean isExpired = false;// 默认没有失效
            boolean isExist = false;// 默认不存在
            StringBuilder sb = new StringBuilder();
            Pattern pattern = Pattern.compile("!\\[.*?\\]\\((.*?)\\)");
            if(target != null){
                // 判断是否存在
                isExist = true;
            }
            if(isExist && currentTime.getTime() - target.getCreationTime().getTime() >= EXPIREDTIME ){
                // 标记是否过期
                isExpired = true;
            }
            if(isExist && isExpired){
                // 存在且过期删除
                historyMapper.deleteById(target);
            }
            if(isExist && !isExpired){
                // 如果存在且没有失活
                com.louis.springbootinit.model.entity.History history = historyMapper.selectOne(historyQueryWrapper);
                String sessionId = history.getSessionId();
                result = streamCall(apiKey, appKey, param.getMessage() + "?结合历史对话：" + his,sessionId,true);
                result
                        .doOnComplete(() -> {
                            // 数据流完成
                            emitter.complete();
                            // 完成回调执行创建任务（保存模型图片链接和替换图片链接）
                            Matcher matcher = pattern.matcher(sb.toString());
                            if (matcher.find()) {
                                String picUrlWithExpired = InciseStrUtils.extractUrl(sb.toString());
                                String picName = extractFilename(picUrlWithExpired);
                                try{
                                    FileUtils.downloadImage(picUrlWithExpired, uploadDir + picName);
                                }catch (Exception e){
                                    emitter.completeWithError(e);
                                }
                                log.info("picUrlWithExpired:" + picUrlWithExpired);
                                log.info("destinationFile:" + uploadDir + picName);
                                saveTaskWithPicUrl(param, user.getId(), picName,picUrlWithExpired);
                            }
                        })
                        .doOnError(e -> {
                            // 在这里可以记录错误日志
                            log.error("An error occurred: ", e);
                            errorOccurred[0] = true; // 设置错误标志
                        })
                        .onErrorReturn(throwable -> null)
                        .doOnNext(data -> {
                            if (data.getOutput().getText() != null) {
                                log.info(data.getOutput().getText());
                                String text = data.getOutput().getText();
                                bean.success(text,200);
                                sb.append(text);
                                String beanJson = JSONUtil.toJsonStr(bean);
                                emitter.send(beanJson);
                            }
                        })
                        .subscribe();
                if(errorOccurred[0]){
                    return null;
                }
            }else{
                // 不存在（第一次开始对话记录）或者sessionId过期
                com.louis.springbootinit.model.entity.History history = new com.louis.springbootinit.model.entity.History();
                history.setCreationTime(new Date());
                history.setUuid(param.getUuid());
                history.setUserId(user.getId().toString());
                AtomicReference<String> sessionId = new AtomicReference<>();
                // 创建会话第一次对话(无需开启上下文记忆)
                result = streamCall(apiKey, appKey, param.getMessage(),true);
                //result = streamCall(apiKey, appKey, param.getMessage(),sessionId,true);
                result
                        .doOnError(e -> {
                            // 在这里可以记录错误日志
                            log.error("An error occurred: ", e);
                            errorOccurred[0] = true; // 设置错误标志
                        })
                        .onErrorReturn(throwable -> null)
                        .doOnNext(data -> {
                            if (data.getOutput().getText() != null) {
                                log.info(data.getOutput().getText());
                                sessionId.set(data.getOutput().getSessionId());
                                String text = data.getOutput().getText();
                                sb.append(text);
                                bean.success(text,200);
                                String beanJson = JSONUtil.toJsonStr(bean);
                                emitter.send(beanJson);
                            }
                        })
                        .doOnComplete(() -> {
                            emitter.complete();// 数据流完成
                            history.setSessionId(sessionId.get());
                            historyMapper.insert(history);
                            Matcher matcher = pattern.matcher(sb.toString());
                            if (matcher.find()) {
                                log.info("==================================有没有数据啊！！！！"+sb.toString());
                                String picUrlWithExpired = InciseStrUtils.extractUrl(sb.toString());
                                String picName = extractFilename(picUrlWithExpired);
                                log.info("picUrlWithExpired:" + picUrlWithExpired);
                                log.info("destinationFile:" + uploadDir + picName);
                                try{
                                    FileUtils.downloadImage(picUrlWithExpired, uploadDir + picName);
                                }catch (Exception e){
                                    emitter.completeWithError(e);
                                }

                                saveTaskWithPicUrl(param, user.getId(), picName,picUrlWithExpired);
                            }
                        })
                        .subscribe();
            }

        }
        catch (Exception e) {
            emitter.completeWithError(e);
        }
        return emitter;
    }

    /**
     * 初始化参数(是否开启增量输出 + 上下文记忆)
     * @param apiKey
     * @param appKey
     * @param prompt
     * @param sessionId
     * @param isIncrementOutput
     * @return
     * @throws NoApiKeyException
     * @throws InputRequiredException
     */
    public  Flowable<ApplicationResult> streamCall(String apiKey,String appKey,String prompt,String sessionId,boolean isIncrementOutput) throws NoApiKeyException, InputRequiredException {
        return aiManager.streamCall(apiKey, appKey, prompt,sessionId,isIncrementOutput);
    }

    /**
     * 初始化参数(是否开启增量输出)
     * @param apiKey
     * @param appKey
     * @param prompt
     * @param isIncrementalOutputOutput
     * @return
     * @throws NoApiKeyException
     * @throws InputRequiredException
     */
    public  Flowable<ApplicationResult> streamCall(String apiKey,String appKey,String prompt,boolean isIncrementalOutputOutput) throws NoApiKeyException, InputRequiredException {
        return aiManager.streamCall(apiKey, appKey, prompt,isIncrementalOutputOutput);
    }

    /**
     * 切割文件名
     * @param url
     * @return
     */
    public String extractFilename(String url) {
        int lastSlashIndex = url.lastIndexOf('/');
        int questionMarkIndex = url.indexOf('?', lastSlashIndex);
        if (lastSlashIndex >= 0 && questionMarkIndex > lastSlashIndex) {
            return url.substring(lastSlashIndex + 1, questionMarkIndex);
        }
        return null;
    }

    /**
     * 保存原图片链接(大模型生图链接)和替换元图片后的链接（服务器直接访问的链接）
     * @param param
     * @param userId
     * @param picName
     * @param rawPicUrl
     * @throws IOException
     */
    public void saveTaskWithPicUrl(ChatRequest param,Long userId,String picName,String rawPicUrl) throws IOException {
        // 拼接要替换的图片url
        String targetPicUrl = imagePrefix + "/images/" + picName;
        // 创建任务（实例化）
        Date date = new Date();
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setStatus(0);// 待执行
        taskInfo.setPrompt(param.getMessage());
        taskInfo.setUuid(param.getUuid());
        taskInfo.setUserId(userId);
        taskInfo.setPicUrl(targetPicUrl);
        taskInfo.setUpdatedAt(date);
        taskInfo.setCreatedAt(date);
        taskInfo.setRawPicUrl(rawPicUrl);
        log.info(taskInfo.toString());
        taskService.saveOrUpdate(taskInfo);// 持久化数据
    }


    /**
     * 每日一问
     * @param modelId
     * @param req
     * @return
     * @throws NoApiKeyException
     * @throws InputRequiredException
     */
    @Override
    public String queryOneQuest(String modelId,HttpServletRequest req) throws NoApiKeyException, InputRequiredException {
        if(Integer.valueOf(modelId) > 5 || Integer.valueOf(modelId) < 0){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        User user = (User)req.getSession().getAttribute(USER_LOGIN_STATE);
        String userRole = user.getUserRole();
        if(userRole.equals(UserConstant.DEFAULT_ROLE)){
            if(!modelId.equals(ModelEnums.MODEL3.getModelId())){
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR,"抱歉，您没有该专题大模型的使用权限！");
            }
        }
        String text = "";
        String modelName;
        List<String> questions = new ArrayList<>();
        if(job.getMapToQuestion() == null || job.getMapToQuestion().size() == 0){
            selectModel(modelId);
            ModelEnums modelEnums = map.get(modelId);
            modelName = modelEnums.getModelName().substring(0,6);
            // List<String> questions = new ArrayList<>();
            try{
                String prompt = "给我提一个有关于"+modelName+"相关领域的问题题目吗?" +
                        "题目格式举例:如何大力发展中医药产业发展? " +
                        "你只需要提供问题题目即可，只要一个，不要有其他的多余的话";
                ApplicationResult call = aiManager.call(apiKey, appKey, prompt);
                text = call.getOutput().getText();
                return text;
            }catch (Exception e){
                // 处理异常并记录日志
                e.printStackTrace();
                return "生成问题时出现错误，请稍后再试。";
            }
        }else{
            Map<String, List<String>> mapToQuestion = job.getMapToQuestion();
            questions = mapToQuestion.getOrDefault(modelId, null);
        }
        int index = RandomUtil.randomInt(5);
        // 每日一问（随机）
        text = questions.get(index);
        return text;
    }

    @Override
    public String replaceImgUrl(String imgUrl, HttpServletRequest req) throws IOException {
        TaskInfo targetUrl = taskService.getOne(new QueryWrapper<TaskInfo>().eq("rawPicUrl", imgUrl));
        if(ObjectUtils.isEmpty(targetUrl)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return targetUrl.getPicUrl();
    }
    /*==================================================下列代码均已废弃，接受者无需理会========================================================*/

    /**
     * 废弃（后来接受者无需理会）
     * @param req
     * @param res
     * @return
     * @throws ApiException
     * @throws NoApiKeyException
     * @throws InputRequiredException
     * @throws IOException
     */
    @Override
    @Deprecated
    public Flux<String> chat(ChatRequest param, HttpServletResponse res, HttpServletRequest req) throws ApiException, NoApiKeyException, InputRequiredException, IOException {
        // 选择选的模型名
        User loginUser = (User)req.getSession().getAttribute(USER_LOGIN_STATE);
        if(!loginUser.getUserRole().equals("user")){
            // 非普通用户可以才看任意选模型
            selectModel(param.getModelId());
        }
        if(!param.getModelId().equals(ModelEnums.MODEL3.getModelId()) && loginUser.getUserRole().equals("user")){
            // 普通用户选用非默认大模型，返回无权限访问
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR,"抱歉，您没有该专题大模型的使用权限！");
        }
        BaiLianResult bean = new BaiLianResult();
        // 响应流
        res.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM_VALUE);
        res.setCharacterEncoding("UTF-8");
        res.setHeader(HttpHeaders.PRAGMA, "no-cache");
        ServletOutputStream out = null;
        // 初始化调用模型
        //ApplicationParam applicationParam = initAppParam();
        ApplicationParam applicationParam = aiManager.initAppParam(appKey, apiKey, param.getMessage());
        out = res.getOutputStream();
        if (StrUtil.isEmpty(param.getMessage())) {
            bean.fail("无效问题，请重新输入",500);
            out.write(JSON.toJSONString(bean).getBytes());
        }
        Application application = new Application();
        StringBuilder answer = new StringBuilder();
        try{
            if(param.getMessage().contains("画") || param.getMessage().contains("照片") || param.getMessage().contains("图片") || param.getMessage().contains("picture")){
                // todo 先用这个顶上（可能需要画图的prompt指令）
                // 采用非流式输出（照片）
                answer = call(application, applicationParam, out);
            }else{
                // 采用流式输出(文字 / 代码)
                answer = streamCall(application, out, applicationParam, answer, bean);
            }
        }
        catch (Exception e){
            log.error("==========catch报错: {}", e.getMessage(), e);
            bean.fail("系统内部错误，请联系管理员",500);
            out.write(JSON.toJSONString(bean).getBytes());
        }finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                log.error("==========finally: {}", e.getMessage(), e);
                bean.fail("系统内部错误，请联系管理员",500);
                out.write(JSON.toJSONString(bean).getBytes());
            }
        }
        String[] characters  = answer.toString().split("");
        return Flux.fromArray(characters).delayElements(Duration.ofMillis(0))
                .map(character -> character.equals(" ") ? " " : character);
    }

    /**
     * 调用流式输出——废弃
     * @param application model应用
     * @param out 输出响应流
     * @param applicationParam model配置
     * @param answer 流式输出结果
     * @param bean 统一响应结果
     * @return
     * @throws NoApiKeyException
     * @throws InputRequiredException
     */
    public StringBuilder streamCall(Application application,ServletOutputStream out,ApplicationParam applicationParam,StringBuilder answer,BaiLianResult bean) throws NoApiKeyException, InputRequiredException {
        Flowable<ApplicationResult> result = application.streamCall(applicationParam);
        ServletOutputStream finalOut = out;
        StringBuilder preText = new StringBuilder();// 没有长度
        result.blockingForEach(data -> {
            // 只输出新增的部分
            String curText = data.getOutput().getText();
            int start = preText.length();
            int end = curText.length();
            StringBuilder sb = new StringBuilder();
            for(int i = start; i < end; i++){
                String add = String.valueOf(curText.charAt(i));
                sb.append(add);// 只获取新增部分
                preText.append(add);// 获取拼接的增量字符的完整内容
            }
            answer.append(sb);
            bean.success(sb.toString(),200);
            finalOut.write(JSON.toJSONString(bean).getBytes());
            finalOut.write("\\n".getBytes());
            finalOut.flush();
        });
        return answer;
    }

    /**
     * 调用非流式输出——废弃
     * @param application
     * @param applicationParam
     * @param out
     * @return
     * @throws NoApiKeyException
     * @throws InputRequiredException
     */
    public StringBuilder call(Application application,ApplicationParam applicationParam, ServletOutputStream out) throws NoApiKeyException, InputRequiredException {
        ApplicationResult result = application.call(applicationParam);
        // 获取图片链接"![](xxxx.png?Expires=xxx&OSSAccessKeyId=XXX&Signature=xxxx)"
        String url = result.getOutput().getText();
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        BaiLianResult bean = new BaiLianResult();
        bean.success(sb.toString(),200);
        try {
            out.write(JSON.toJSONString(bean).getBytes());
            out.flush();
        } catch (IOException e) {
            // 处理可能的IO异常
            e.printStackTrace();
        }
        return sb;
    }

    /**
     * 测试版(单指令聊天服务)——废弃
     * @param prompt
     * @param res
     * @return
     * @throws ApiException
     * @throws NoApiKeyException
     * @throws InputRequiredException
     * @throws IOException
     */
    @Override
    @Deprecated
    public Flux<String> chat(
            String prompt,
            HttpServletResponse res) throws ApiException, NoApiKeyException, InputRequiredException, IOException {
        selectModel("中医药产业大模型");
        BaiLianResult bean = new BaiLianResult();
        // 响应流
        res.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM_VALUE);
        res.setCharacterEncoding("UTF-8");
        res.setHeader(HttpHeaders.PRAGMA, "no-cache");
        ServletOutputStream out = null;
        // 初始化调用模型
        ApplicationParam param = ApplicationParam.builder()
                .apiKey(apiKey)
                .appId(appKey)
                .prompt(prompt)
                .build();
        out = res.getOutputStream();
        if (StrUtil.isEmpty(prompt)) {
            bean.fail("无效问题，请重新输入",500);
            out.write(JSON.toJSONString(bean).getBytes());
        }
        Application application = new Application();
        StringBuilder answer = new StringBuilder();

        try{
            Flowable<ApplicationResult> result = application.streamCall(param);
            ServletOutputStream finalOut = out;
            StringBuilder preText = new StringBuilder();// 没有长度
            //
            result.blockingForEach(data -> {
                // 只输出新增的部分
                String curText = data.getOutput().getText();
                int start = preText.length();
                int end = curText.length();
                StringBuilder sb = new StringBuilder();
                for(int i = start; i < end; i++){
                    String add = String.valueOf(curText.charAt(i));
                    sb.append(add);// 只获取新增部分
                    preText.append(add);// 获取拼接的增量字符的完整内容
                }
                answer.append(sb);
                bean.success(sb.toString(),200);
                finalOut.write(JSON.toJSONString(bean).getBytes());
                finalOut.flush();
            });
        }
        catch (Exception e){
            bean.fail("系统内部错误，请联系管理员",500);
            out.write(JSON.toJSONString(bean).getBytes());
        }finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                bean.fail("系统内部错误，请联系管理员",500);
                out.write(JSON.toJSONString(bean).getBytes());
            }
        }
        String[] characters  = answer.toString().split("");
        return Flux.fromArray(characters).delayElements(Duration.ofMillis(0))
                .map(character -> character.equals(" ") ? " " : character);
    }

    /**
     * 废弃
     * @param prompt
     * @param modelId
     * @param req
     * @param emitter
     * @return
     */
    @Override
    @Deprecated
    public SseEmitter sseChat(String prompt, String modelId, HttpServletRequest req, SseEmitter emitter) {
        // Simulate asynchronous data retrieval from the database
        selectModel(modelId);
        new Thread(() -> {
            try {
                //ApplicationParam applicationParam = initAppParam(apiKey, appKey, prompt);
                Flowable<ApplicationResult> result = aiManager.streamCall(apiKey, appKey, prompt,"");
                //Application application = new Application();
                //Flowable<ApplicationResult> result = application.streamCall(applicationParam);
                StringBuilder preText = new StringBuilder();
                BaiLianResult bean = new BaiLianResult();
                result.blockingForEach(data -> {
                    // 只输出新增的部分
                    String curText = data.getOutput().getText();
                    int start = preText.length();
                    int end = curText.length();
                    StringBuilder sb = new StringBuilder();
                    for(int i = start; i < end; i++){
                        String add = String.valueOf(curText.charAt(i));
                        sb.append(add);// 只获取新增部分
                        preText.append(add);// 获取拼接的增量字符的完整内容
                    }
                    bean.success(sb.toString(),200);
                    emitter.send(bean);
                    Thread.sleep(100);
                });
                emitter.complete(); // Complete the SSE connection
            } catch (Exception e) {
                emitter.completeWithError(e); // Handle errors
            }
        }).start();
        return emitter;
    }

}
