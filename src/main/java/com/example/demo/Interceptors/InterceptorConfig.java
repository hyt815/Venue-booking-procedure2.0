package com.example.demo.Interceptors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer
{
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(new JWTInterceptor()).addPathPatterns("/**");//全部路径
                //.excludePathPatterns("/admin/**","/student/register","/student/appointment");//开放登录路径排除不需要Token的路径
    }
}
