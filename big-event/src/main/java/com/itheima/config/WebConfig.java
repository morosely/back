package com.itheima.config;

import com.itheima.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-folder}")
    private String UPLOAD_FOLDER;

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登录接口和注册接口,上传不拦截
        registry.addInterceptor(loginInterceptor).excludePathPatterns("/user/login","/user/register","/images/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //图片资源映射:其中/images是访问图片资源的前缀，比如要访问test1.png。则全路径为：http://localhost:端口号/images/test1.png
        registry.addResourceHandler("/images/**").addResourceLocations("file:/"+UPLOAD_FOLDER);//此处为设置服务端存储图片的路径（前段上传到后台的图片保存位置）
    }
}
