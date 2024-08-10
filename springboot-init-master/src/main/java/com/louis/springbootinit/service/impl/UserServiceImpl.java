package com.louis.springbootinit.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.louis.springbootinit.common.ErrorCode;
import com.louis.springbootinit.constant.UserConstant;
import com.louis.springbootinit.model.dto.user.*;
import com.louis.springbootinit.model.enums.ModelEnums;
import com.louis.springbootinit.model.enums.SmsTypeEnum;
import com.louis.springbootinit.model.vo.AuthModelVO;
import com.louis.springbootinit.utils.EmailUtils;
import com.louis.springbootinit.utils.JwtTokenUtils;
import com.louis.springbootinit.utils.SmsUtils;
import com.louis.springbootinit.constant.CommonConstant;
import com.louis.springbootinit.exception.BusinessException;
import com.louis.springbootinit.mapper.UserMapper;
import com.louis.springbootinit.model.entity.User;
import com.louis.springbootinit.model.enums.UserRoleEnum;
import com.louis.springbootinit.model.vo.LoginUserVO;
import com.louis.springbootinit.model.vo.UserVO;
import com.louis.springbootinit.service.UserService;
import com.louis.springbootinit.utils.SqlUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.tencentcloudapi.rce.v20201103.models.QQAccountInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import static com.louis.springbootinit.constant.UserConstant.*;

/**
 * 用户服务实现
 
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "louis";

    @Resource
    private SmsUtils smsUtils;

    @Resource
    private EmailUtils emailUtils;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public long userRegister(UserRegisterRequest userRegisterRequest) {
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userPassword.length() < PASSWORD_LENGTH_LIMIT) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        synchronized (userAccount.intern()) {
            if (EmailUtils.isValidEmail(userAccount)) {
                if(!emailUtils.validCode(userAccount,userRegisterRequest.getCode())){
                    throw new BusinessException(ErrorCode.PARAMS_ERROR,"验证码错误");
                }
                EmailUtils.EMAIL_MESSAGE_CACHE_MAP.remove(userAccount);
            }else{
                if (userAccount.length() != 11) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户格式错误");
                }
                if(!smsUtils.validCode(SmsTypeEnum.REGISTER,userAccount,userRegisterRequest.getCode())){
                    throw new BusinessException(ErrorCode.PARAMS_ERROR,"验证码错误");
                }
                SmsUtils.SMS_REGISTER_CACHE_BO_MAP.remove(userAccount);
            }

            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            // 2. 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            // 3. 插入数据
            User user = new User();
            List<String> authModelList = new ArrayList<>();
            // 默认所有用户都含有对行业大模型的使用权限
            authModelList.add(ModelEnums.MODEL3.getModelId());
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            user.setUserName(userRegisterRequest.getUserName());
            user.setAuthModelVos(authModelList);
            // 注册用户默认为普通用户
            user.setUserRole(UserRoleEnum.USER.getValue());
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        }
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userPassword.length() < UserConstant.PASSWORD_LENGTH_LIMIT) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 3. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        String token = JwtTokenUtils.generateToken(userAccount);
        request.getSession().setAttribute(USER_LOGIN_TOKEN,token);
        JwtTokenUtils.LOGIN_TOKEN_MAP.put(userAccount,token);
        return this.getLoginUserVO(user);
    }

    @Override
    public LoginUserVO userNoSecretLogin(UserNoSecretLoginReqDTO reqDTO, HttpServletRequest request) {
        String userAccount = reqDTO.getUserAccount();
        String userName;
        synchronized (userAccount.intern()) {
            if (EmailUtils.isValidEmail(reqDTO.getUserAccount())) {
                if(!emailUtils.validCode(userAccount,reqDTO.getCode())){
                    throw new BusinessException(ErrorCode.PARAMS_ERROR,"验证码错误");
                }
                EmailUtils.EMAIL_MESSAGE_CACHE_MAP.remove(userAccount);
                userName = userAccount.substring(userAccount.indexOf('@') - 4);
            }else{
                if (reqDTO.getUserAccount().length() != 11) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户格式错误");
                }
                if(!smsUtils.validCode(SmsTypeEnum.LOGIN,userAccount,reqDTO.getCode())){
                    throw new BusinessException(ErrorCode.PARAMS_ERROR,"验证码错误");
                }
                SmsUtils.SMS_LOGIN_CACHE_BO_MAP.remove(userAccount);
                userName = userAccount.substring(userAccount.length() - 4);
            }

            User user = this.lambdaQuery().eq(User::getUserAccount, userAccount).one();
            // 如果没有当前邮箱，则创建账号并登录
            if(Objects.isNull(user)){
                // 3. 插入数据
                user = new User();
                user.setUserAccount(userAccount);

                user.setUserName(userName);
                user.setUserPassword(DigestUtils.md5DigestAsHex((SALT + UserConstant.DEFAULT_PASSWORD).getBytes()));
                // 注册用户默认为普通用户
                user.setUserRole(UserRoleEnum.USER.getValue());
                this.save(user);
            }
            request.getSession().setAttribute(USER_LOGIN_STATE, user);
            String token = JwtTokenUtils.generateToken(userAccount);
            request.getSession().setAttribute(USER_LOGIN_TOKEN,token);
            JwtTokenUtils.LOGIN_TOKEN_MAP.put(userAccount,token);
            return this.getLoginUserVO(user);
        }
    }

    @Override
    public boolean sendEmailCode(String userAccount) {
        return emailUtils.sendCode(userAccount);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 判断是否被其他设备登录
        Object token = request.getSession().getAttribute(USER_LOGIN_TOKEN);
        if(Objects.nonNull(token)){
            String loginToken = token.toString();
            String userToken = JwtTokenUtils.LOGIN_TOKEN_MAP.getOrDefault(currentUser.getUserAccount(), null);
            if(!loginToken.equals(userToken)){
                throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"账号已在其他地方登录");
            }
        }

        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUserPermitNull(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            return null;
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        return this.getById(userId);
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return isAdmin(user);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        request.getSession().removeAttribute(USER_LOGIN_TOKEN);
        return true;
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String unionId = userQueryRequest.getUnionId();
        String mpOpenId = userQueryRequest.getMpOpenId();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(unionId), "unionId", unionId);
        queryWrapper.eq(StringUtils.isNotBlank(mpOpenId), "mpOpenId", mpOpenId);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StringUtils.isNotBlank(userProfile), "userProfile", userProfile);
        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public void updatePassword(UserUpdatePasswordReqDTO reqDTO) {
        String userAccount = reqDTO.getUserAccount();
        User user = this.lambdaQuery().eq(User::getUserAccount, userAccount).one();
        if(Objects.isNull(user)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"没有当前账户");
        }
        String dbPassword = DigestUtils.md5DigestAsHex((SALT + reqDTO.getOriginalPassword()).getBytes());
        String userPassword = user.getUserPassword();
        if(!dbPassword.equals(userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"原密码错误");
        }

        user.setUserPassword(DigestUtils.md5DigestAsHex((SALT + reqDTO.getNewPassword()).getBytes()));
        Assert.isTrue(this.updateById(user),()-> new BusinessException(ErrorCode.SYSTEM_ERROR,"修改密码失败"));
    }

    @Override
    public void noSecretPasswordUpdate(UserNoSecretUpdatePasswordReqDTO reqDTO) {
        String userAccount = reqDTO.getUserAccount();
        EmailUtils emailUtils = new EmailUtils();
        User user = this.lambdaQuery().eq(User::getUserAccount, userAccount).one();
        if(Objects.isNull(user)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"没有当前账户");
        }
        synchronized (userAccount.intern()) {
            if (EmailUtils.isValidEmail(userAccount)) {
                if(!emailUtils.validCode(userAccount,reqDTO.getCode())){
                    throw new BusinessException(ErrorCode.PARAMS_ERROR,"验证码错误");
                }
                EmailUtils.EMAIL_MESSAGE_CACHE_MAP.remove(userAccount);
            }else{
                if (userAccount.length() != 11) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户格式错误");
                }
                if(!smsUtils.validCode(SmsTypeEnum.UPDATE_PASSWORD,userAccount,reqDTO.getCode())){
                    throw new BusinessException(ErrorCode.PARAMS_ERROR,"验证码错误");
                }
                SmsUtils.SMS_UPDATE_PASSWORD_BO_MAP.remove(userAccount);
            }
            user.setUserPassword(DigestUtils.md5DigestAsHex((SALT + reqDTO.getPassword()).getBytes()));
            Assert.isTrue(this.updateById(user),()-> new BusinessException(ErrorCode.SYSTEM_ERROR,"修改密码失败"));
        }

    }

    @Override
    public void sendPhoneCode(SmsTypeEnum smsTypeEnum, String userAccount) {
        smsUtils.sendCode(smsTypeEnum,userAccount);
    }

    /**
     * 当前用户是否有使用模型权限
     * @param modelId
     * @return
     */
    @Override
    public boolean isAuthByModel(String userId,String modelId) {
//        User loginUser = this.getById(userId);
//        List<AuthModelVO> vos = loginUser.getAuthModelVos();
//        if(vos.isEmpty()){
//            // 适配之前用户数据
//            if (loginUser.getUserRole().equals(UserRoleEnum.ADMIN.getText())) {
//                // 如果相等（管理员用户可以使用所有模型）
//                return true;
//            }else{
//                // 普通用户
//                if (!ModelEnums.MODEL3.getModelId().equals(modelId)) {
//                    // 普通用户只能用模型3（行业大模型）
//                    return false;
//                }else{
//                    return true;
//                }
//            }
//        }
//        AuthModelVO authModelVO = vos.stream().filter(vo -> modelId.equals(vo.getModelId()))
//                .collect(Collectors.toList()).get(0);
        return false;
    }

    @Override
    public String uploadAvatar(MultipartFile image, Long userId) {
        User loginUser = this.getById(userId);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        if (image.isEmpty()) {
            return "图片为空";
        }

        String originalFilename = image.getOriginalFilename();
        if (originalFilename == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "原始文件名为空");
        }
        String fileNamePrefix = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String[] filenameParts = originalFilename.split("\\.");
        if (filenameParts.length < 2) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "文件名格式不正确");
        }
        String fileNameSuffix = "." + filenameParts[1];
        String fileName = fileNamePrefix + fileNameSuffix;

        ApplicationHome applicationHome = new ApplicationHome(this.getClass());
        File homeDir = applicationHome.getDir();
        log.info("homeDir: " + (homeDir != null ? homeDir.getAbsolutePath() : "null"));
        log.info("homeDir.getParentFile(): " + (homeDir != null && homeDir.getParentFile() != null ? homeDir.getParentFile().getAbsolutePath() : "null"));
        log.info("homeDir.getParentFile().getParentFile(): " + (homeDir != null && homeDir.getParentFile() != null && homeDir.getParentFile().getParentFile() != null ? homeDir.getParentFile().getParentFile().getAbsolutePath() : "null"));
        log.info("homeDir.getParentFile().getParentFile().getParentFile(): " + (homeDir != null && homeDir.getParentFile() != null && homeDir.getParentFile().getParentFile() != null && homeDir.getParentFile().getParentFile().getParentFile() != null ? homeDir.getParentFile().getParentFile().getParentFile().getAbsolutePath() : "null"));
        log.info("homeDir.getParentFile().getParentFile().getParentFile().getParentFile(): " + (homeDir != null && homeDir.getParentFile() != null && homeDir.getParentFile().getParentFile() != null && homeDir.getParentFile().getParentFile().getParentFile() != null && homeDir.getParentFile().getParentFile().getParentFile().getParentFile() != null ? homeDir.getParentFile().getParentFile().getParentFile().getParentFile().getAbsolutePath() : "null"));
        if (homeDir == null){
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "目录结构错误");
        }
        String pre = (homeDir != null ? homeDir.getAbsolutePath() : "null") + "imageUrlSuffix";
        log.info("存储的根目录：" + pre);
        String path = pre + fileName;
        log.info("服务器存储地址：" + path);

        File file = new File(path);
        if (file == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "存储图片路径不存在");
        }

        try {
            image.transferTo(file);
            String replace = path.replace(homeDir.getAbsolutePath() + "\\spring-ui\\src", "/src");
            log.info("url地址：" + replace);
            String imgUrl = replace.replace("\\", "/");
            log.info("服务器存储地址：" + imgUrl);
            imgUrl = "http://localhost:8102/images/" + fileName;
            loginUser.setUserAvatar(imgUrl);
            this.updateById(loginUser);
            return imgUrl;
        } catch (IOException e) {
            e.printStackTrace();
            return "图片上传失败";
        }
    }

}
