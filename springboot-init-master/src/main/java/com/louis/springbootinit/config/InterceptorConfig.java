package com.louis.springbootinit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author louis
 * @version 1.0
 * @date 2024/7/16 22:20
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //图片资源映射　　　　　//其中/images是访问图片资源的前缀，比如要访问test1.png。则全路径为：http://localhost:端口号/images/test1.png
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:/data/images/");
        // 文件资源映射
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:/data/files/");
        // 用户上传图片给大模型资源映射
        registry.addResourceHandler("/pics/**")
                .addResourceLocations("file:/data/pics/");
    }
}
