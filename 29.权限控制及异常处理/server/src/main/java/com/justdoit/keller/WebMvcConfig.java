package com.justdoit.keller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import javax.annotation.Resource;


@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {


    @Resource
    private MyInterceptor myInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor)
                .addPathPatterns("/admin/**")
                .addPathPatterns("/note/**")
                .addPathPatterns("/notes/**")
                .addPathPatterns("/userCard/**")
                .addPathPatterns("/user/**");
    }
}
