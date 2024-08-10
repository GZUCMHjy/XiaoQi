package com.louis.springbootinit.controller;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.louis.springbootinit.annotation.AuthCheck;
import com.louis.springbootinit.common.BaseResponse;
import com.louis.springbootinit.common.ErrorCode;
import com.louis.springbootinit.common.ResultUtils;
import com.louis.springbootinit.constant.UserConstant;
import com.louis.springbootinit.exception.BusinessException;
import com.louis.springbootinit.mapper.HistoryDataMapper;
import com.louis.springbootinit.model.dto.ChatData;
import com.louis.springbootinit.model.dto.Conversation;
import com.louis.springbootinit.model.dto.ConversationVO;
import com.louis.springbootinit.model.entity.HistoryData;
import com.louis.springbootinit.model.entity.MsgLog;
import com.louis.springbootinit.model.entity.User;
import com.louis.springbootinit.service.HistoryDataService;
import com.louis.springbootinit.service.MsgLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static com.louis.springbootinit.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author louis
 * @version 1.0
 * @date 2024/5/25 23:27
 */
@RestController
@RequestMapping("/api/conversation")
@Api(value = "对话模块", tags = "对话模块")
@Slf4j
public class ChatConversationController {

    @Resource
    private MsgLogService msgLogService;

    @Resource
    private HistoryDataMapper historyDataMapper;

    /**
     * 保存用户-模型的历史记录
     * @param req
     * @param conversationVO
     * @return
     * @throws Exception
     * @throws ApiException
     * @throws NoApiKeyException
     * @throws InputRequiredException
     */
    @PostMapping(value = "/saveConversation")
    @ApiOperation(value = "保存用户-模型的历史记录")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<Boolean> saveConversation(HttpServletRequest req, @RequestBody ConversationVO conversationVO) throws Exception, ApiException, NoApiKeyException, InputRequiredException {
        User loginUser = (User)req.getSession().getAttribute(USER_LOGIN_STATE);
        HistoryData historyData = new HistoryData();
        // todo 大对象容易内存溢出（oom）
        String jsonStr = JSONUtil.toJsonStr(conversationVO);
        historyData.setContext(jsonStr);
        historyData.setUserid(loginUser.getId().toString());
        QueryWrapper<HistoryData> historyDataQueryWrapper = new QueryWrapper<>();
        historyDataQueryWrapper.eq("userId",loginUser.getId().toString());
        boolean exists = historyDataMapper.exists(historyDataQueryWrapper);
        if(exists){
            if(historyDataMapper.update(historyData,historyDataQueryWrapper) < 0){
                throw new BusinessException(ErrorCode.OPERATION_ERROR);
            }
        }else{
            if(historyDataMapper.insert(historyData) < 0){
                throw new BusinessException(ErrorCode.OPERATION_ERROR);
            }
        }
        return ResultUtils.success(Boolean.TRUE);
    }
    /*================================================以下代码均已废弃，接手者无需理会===============================================*/

    /**
     * 获取用户与当前选用模型的聊天窗口列表——废弃
     * @param req
     * @return
     */
    @GetMapping(value = "/queryUserModelConversation")
    @ApiOperation(value = "获取用户-模型的历史记录")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    @Deprecated
    public BaseResponse<?> queryUserModelConversation(HttpServletRequest req){
        User loginUser = (User)req.getSession().getAttribute(USER_LOGIN_STATE);
        QueryWrapper<HistoryData> historyDataQueryWrapper = new QueryWrapper<>();
        HistoryData historyData = historyDataMapper.selectOne(historyDataQueryWrapper.eq("userId", loginUser.getId().toString()));
        if(historyData == null){
            // 新用户暂无历史记录
            return ResultUtils.success(null);
        }
        String context = historyData.getContext();
        ConversationVO bean = JSONUtil.toBean(context, ConversationVO.class);
        // 返回当前创建后的窗口
        return ResultUtils.success(bean);
    }
    /**
     * 获取用户-模型的历史记录——废弃
     * @param req
     * @param modelId
     * @return
     */
    @GetMapping(value = "/queryUserConversation")
    @ApiOperation(value = "获取用户-模型的历史记录",hidden = true)
    @Deprecated
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<ConversationVO> queryUserConversation(HttpServletRequest req, String modelId) {
        User loginUser = (User)req.getSession().getAttribute(USER_LOGIN_STATE);
        if(loginUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"请登录");
        }
        Map<String,List<ChatData>> chatDataMap = msgLogService.queryUserAllConversation(String.valueOf(loginUser.getId()),modelId);
        List<Conversation> conversations = msgLogService.queryUserModelConversation(loginUser.getId(), modelId);
        ConversationVO ans = new ConversationVO();
        ans.setConversationList(conversations);
        ans.setChatDataMap(chatDataMap);
        // 返回当前创建后的窗口
        return ResultUtils.success(ans);
    }



    /**
     * 获取当前用户对话窗口的历史对话数据——废弃
     * @param req
     * @param uuid
     * @return
     */
    @GetMapping(value = "/queryUserCurrentConversation")
    @ApiOperation(value = "获取当前用户对话窗口的历史对话数据",hidden = true)
    @Deprecated
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Map<String,List<ChatData>>> queryUserCurrentConversation(HttpServletRequest req, String uuid) {
        User loginUser = (User)req.getSession().getAttribute(USER_LOGIN_STATE);
        if(loginUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"请登录");
        }
        Map<String,List<ChatData>> chatDataMap = msgLogService.queryUserCurrentConversation(uuid);
        // 返回当前创建后的窗口
        return ResultUtils.success(chatDataMap);
    }

    /**
     * 废弃
     * @param req
     * @return
     */
    @GetMapping(value = "/queryConversation")
    @ApiOperation(value = "获取当前用户对话窗口的历史对话数据",hidden = true)
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<ConversationVO> queryConversation(HttpServletRequest req) {
        User loginUser = (User)req.getSession().getAttribute(USER_LOGIN_STATE);
        HistoryData historyData = historyDataMapper.selectById(loginUser.getId());
        String context = historyData.getContext();
        ConversationVO conversationVO = JsonUtils.fromJson(context, ConversationVO.class);
        // 返回当前创建后的窗口
        return ResultUtils.success(conversationVO);
    }
}
