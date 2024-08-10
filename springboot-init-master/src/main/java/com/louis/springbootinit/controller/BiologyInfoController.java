package com.louis.springbootinit.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.louis.springbootinit.annotation.AuthCheck;
import com.louis.springbootinit.common.BaseResponse;
import com.louis.springbootinit.common.ErrorCode;
import com.louis.springbootinit.common.ResultUtils;
import com.louis.springbootinit.constant.UserConstant;
import com.louis.springbootinit.exception.BusinessException;
import com.louis.springbootinit.mapper.*;
import com.louis.springbootinit.model.dto.bio.*;
import com.louis.springbootinit.model.dto.request.ChatRequest;
import com.louis.springbootinit.model.entity.*;
import com.louis.springbootinit.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.text.Format;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.louis.springbootinit.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author louis
 * @version 1.0
 * @date 2024/6/7 20:50
 */
@RestController
@RequestMapping("/api/bioInfo")
@Api(value = "生物信息模块", tags = "生物信息模块")
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
@Validated
@Slf4j
public class BiologyInfoController {

    private final BiomarkersMapper biomarkersMapper;
    private final BodyHealthMapper bodyHealthMapper;
    private final HealthBehaviorsMapper healthBehaviorsMapper;
    private final MedicalRecordsMapper medicalRecordsMapper;
    private final MentalHealthMapper mentalHealthMapper;
    private final UserService userService;
    @PostMapping(value = "/save")
    @ApiOperation(value = "保存个人生物信息")
    @Transactional
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<?> save(@RequestBody @Valid BioAddRequestDTO bioAddRequest, HttpServletRequest req){
        User loginUser = (User)req.getSession().getAttribute(USER_LOGIN_STATE);
        loginUser.setUserName(bioAddRequest.getUserName());
        // 更新个人信息名
        userService.updateById(loginUser);
        String userId = String.valueOf(loginUser.getId());
        // 生物标志物
        BiomarkersDTO biomarkers = bioAddRequest.getBiomarkers();
        Biomarkers biomarkersEntity = new Biomarkers();
        BeanUtils.copyProperties(biomarkers,biomarkersEntity);
        biomarkersEntity.setUserId(userId);
        QueryWrapper<Biomarkers> biomarkersQueryWrapper = new QueryWrapper<>();
        log.info("biomarkersQueryWrapper");
        biomarkersQueryWrapper.eq("userId",userId);
        if(biomarkersMapper.exists(biomarkersQueryWrapper)){
            biomarkersEntity.setUpdateTime(new Date());
            biomarkersMapper.updateById(biomarkersEntity);
        }else{
            biomarkersEntity.setUpdateTime(new Date());
            biomarkersEntity.setCreateTime(new Date());
            biomarkersMapper.insert(biomarkersEntity);
        }

        // 身体健康
        BodyHealthDTO bodyHealth = bioAddRequest.getBodyHealth();
        BodyHealth bodyHealthEntity = new BodyHealth();
        BeanUtils.copyProperties(bodyHealth,bodyHealthEntity);
        bodyHealthEntity.setUserId(userId);
        log.info("bodyHealthQueryWrapper");
        QueryWrapper<BodyHealth> bodyHealthQueryWrapper = new QueryWrapper<>();
        bodyHealthQueryWrapper.eq("userId",userId);
        if (bodyHealthMapper.exists(bodyHealthQueryWrapper)) {
            bodyHealthEntity.setUpdateTime(new Date());
            bodyHealthMapper.updateById(bodyHealthEntity);
        }else{
            bodyHealthEntity.setUpdateTime(new Date());
            bodyHealthEntity.setCreateTime(new Date());
            bodyHealthMapper.insert(bodyHealthEntity);
        }

        // 健康行为
        HealthBehaviorsDTO healthBehaviors = bioAddRequest.getHealthBehaviors();
        HealthBehaviors healthBehaviorsEntity = new HealthBehaviors();
        BeanUtils.copyProperties(healthBehaviors,healthBehaviorsEntity);
        healthBehaviorsEntity.setUserId(userId);
        QueryWrapper<HealthBehaviors> healthBehaviorsQueryWrapper = new QueryWrapper<>();
        healthBehaviorsQueryWrapper.eq("userId",userId);
        log.info("healthBehaviorsQueryWrapper");
        if (healthBehaviorsMapper.exists(healthBehaviorsQueryWrapper)) {
            healthBehaviorsEntity.setUpdateTime(new Date());
            healthBehaviorsMapper.updateById(healthBehaviorsEntity);
        }else{
            healthBehaviorsEntity.setUpdateTime(new Date());
            healthBehaviorsEntity.setCreateTime(new Date());
            healthBehaviorsMapper.insert(healthBehaviorsEntity);
        }
        // 心理健康
        MentalHealthDTO mentalHealth = bioAddRequest.getMentalHealth();
        MentalHealth mentalHealthEntity = new MentalHealth();
        BeanUtils.copyProperties(mentalHealth,mentalHealthEntity);
        mentalHealthEntity.setUserId(userId);
        QueryWrapper<MentalHealth> mentalHealthQueryWrapper = new QueryWrapper<>();
        mentalHealthQueryWrapper.eq("userId",userId);
        log.info("mentalHealthQueryWrapper");
        if (mentalHealthMapper.exists(mentalHealthQueryWrapper)) {
            mentalHealthEntity.setUpdateTime(new Date());
            mentalHealthMapper.updateById(mentalHealthEntity);
        }else{
            mentalHealthEntity.setUpdateTime(new Date());
            mentalHealthEntity.setCreateTime(new Date());
            mentalHealthMapper.insert(mentalHealthEntity);
        }

        // 医疗记录表
        MedicalRecordsDTO medicalRecords = bioAddRequest.getMedicalRecords();
        MedicalRecords medicalRecordsEntity = new MedicalRecords();
        BeanUtils.copyProperties(medicalRecords,medicalRecordsEntity);
        medicalRecordsEntity.setUserId(userId);
        QueryWrapper<MedicalRecords> medicalRecordsQueryWrapper = new QueryWrapper<>();
        medicalRecordsQueryWrapper.eq("userId",userId);
        log.info("medicalRecordsQueryWrapper");
        if (medicalRecordsMapper.exists(medicalRecordsQueryWrapper)) {
            medicalRecordsEntity.setUpdateTime(new Date());
            medicalRecordsMapper.updateById(medicalRecordsEntity);
        }else{
            medicalRecordsEntity.setUpdateTime(new Date());
            medicalRecordsEntity.setCreateTime(new Date());
            medicalRecordsMapper.insert(medicalRecordsEntity);
        }
        return ResultUtils.success(Boolean.TRUE);
    }
    @PostMapping(value = "/query")
    @ApiOperation(value = "获取个人生物信息")
    @Transactional
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<BioQueryRequestDTO> query(HttpServletRequest req){
        User loginUser = (User)req.getSession().getAttribute(USER_LOGIN_STATE);
        String userId = String.valueOf(loginUser.getId());
        // 从数据库查
        User user = userService.getOne(new QueryWrapper<User>().eq("id", userId));
        Biomarkers biomarkers = biomarkersMapper.selectById(userId);
        BodyHealth bodyHealth = bodyHealthMapper.selectById(userId);
        HealthBehaviors healthBehaviors = healthBehaviorsMapper.selectById(userId);
        MentalHealth mentalHealth = mentalHealthMapper.selectById(userId);
        MedicalRecords medicalRecords = medicalRecordsMapper.selectById(userId);
        if(ObjectUtil.isAllEmpty(biomarkers,bodyHealth,healthBehaviors,mentalHealth,medicalRecords)){
            return ResultUtils.success(null);
        }
        Date latestUpdateTime = getLatestUpdateTime(biomarkers, bodyHealth, healthBehaviors, mentalHealth, medicalRecords);
        BioQueryRequestDTO bioQueryRequestDTO = new BioQueryRequestDTO();
        BiomarkersDTO biomarkersDTO = new BiomarkersDTO();
        BeanUtils.copyProperties(biomarkers,biomarkersDTO);
        // 回显修改后的用户名
        bioQueryRequestDTO.setUserName(user.getUserName());
        bioQueryRequestDTO.setBiomarkers(biomarkersDTO);

        BodyHealthDTO bodyHealthDTO = new BodyHealthDTO();
        BeanUtils.copyProperties(bodyHealth,bodyHealthDTO);
        bioQueryRequestDTO.setBodyHealth(bodyHealthDTO);

        HealthBehaviorsDTO healthBehaviorsDTO = new HealthBehaviorsDTO();
        BeanUtils.copyProperties(healthBehaviors,healthBehaviorsDTO);
        bioQueryRequestDTO.setHealthBehaviors(healthBehaviorsDTO);

        MentalHealthDTO mentalHealthDTO = new MentalHealthDTO();
        BeanUtils.copyProperties(mentalHealth,mentalHealthDTO);
        bioQueryRequestDTO.setMentalHealth(mentalHealthDTO);

        MedicalRecordsDTO medicalRecordsDTO = new MedicalRecordsDTO();
        BeanUtils.copyProperties(medicalRecords,medicalRecordsDTO);
        bioQueryRequestDTO.setMedicalRecords(medicalRecordsDTO);
        bioQueryRequestDTO.setUpdateTime(latestUpdateTime);
        return ResultUtils.success(bioQueryRequestDTO);
    }
    public Date getLatestUpdateTime(Biomarkers biomarkers, BodyHealth bodyHealth, HealthBehaviors healthBehaviors, MentalHealth mentalHealth, MedicalRecords medicalRecords) {
        // 创建一个列表来存储所有的updateTime
        List<Date> times = new ArrayList<>();
        times.add(biomarkers.getUpdateTime());
        times.add(bodyHealth.getUpdateTime());
        times.add(healthBehaviors.getUpdateTime());
        times.add(mentalHealth.getUpdateTime());
        times.add(medicalRecords.getUpdateTime());

        // 使用Stream API来找到最大的（即最近的）时间
        Optional<Date> latestUpdateTime = times.stream()
                .max(Date::compareTo); // 使用Date的compareTo方法来找到最大的时间

        // 如果列表为空或所有对象都没有updateTime（这在实际中不太可能，因为我们已经添加了它们），
        // 则返回null或默认值
        return latestUpdateTime.orElse(null); // 或者你可以提供一个默认值，如 new Date(0)
    }

}
