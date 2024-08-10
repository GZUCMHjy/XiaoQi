package com.louis.springbootinit.controller;

import com.louis.springbootinit.common.BaseResponse;
import com.louis.springbootinit.common.ErrorCode;
import com.louis.springbootinit.common.ResultUtils;
import com.louis.springbootinit.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author louis
 * @version 1.0
 * @date 2024/7/28 14:03
 * 该接口层停用
 * ps:该接口层为后续多模态大模型接口调用做准备的（todo）
 */
@RestController
@RequestMapping("/api/upload")
@Api(value = "文件上传模块", tags = "文件上传模块")
@Deprecated
public class FileUploadController {

    @Value("${app.file.upload-dir}")
    private String uploadDir;
    @Value("${app.upload.prefix}")
    private String filePrefix;
    @Value("${app.file.upload-pic}")
    private String uploadPic;

    /**
     * 文件批量(含单个)上传
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("/multiple")
    @ApiOperation(value = "文件批量(含单个)上传")
    public BaseResponse<List<String>> uploadMultipleFiles(MultipartHttpServletRequest request) throws IOException {
        List<MultipartFile> files = request.getFiles("files");
        long totalSize = files.stream().mapToLong(MultipartFile::getSize).sum();

        if (totalSize > 150 * 1024 * 1024) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"文件总大小超过150MB.");
        }
        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            // 时间戳
            String fileNamePrefix = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            String filePath = uploadDir + fileNamePrefix + "-" +file.getOriginalFilename();
            filePath = filePath.replace("\\", "/");
            File dest = new File(filePath);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
            // 文件访问路径
            String visitFileUrl = filePrefix + "/files/" + fileNamePrefix + "-" +file.getOriginalFilename();
            fileUrls.add(visitFileUrl);
        }
        return ResultUtils.success(fileUrls);
    }

    /**
     * 图片批量(含单个)上传
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("/pictures")
    @ApiOperation(value = "图片批量(含单个)上传")
    public BaseResponse<List<String>> uploadMultiplePics(MultipartHttpServletRequest request) throws IOException {
        List<MultipartFile> files = request.getFiles("pics");
        long totalSize = files.stream().mapToLong(MultipartFile::getSize).sum();

        if (totalSize > 150 * 1024 * 1024) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"文件总大小超过150MB.");
        }
        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            // 时间戳
            String fileNamePrefix = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            String filePath = uploadPic + fileNamePrefix + "-" +file.getOriginalFilename();
            filePath = filePath.replace("\\", "/");
            File dest = new File(filePath);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
            // 用户聊天上传图片的访问路径
            String visitFileUrl = filePrefix + "/pics/" + fileNamePrefix + "-" +file.getOriginalFilename();
            fileUrls.add(visitFileUrl);
        }
        return ResultUtils.success(fileUrls);
    }
}