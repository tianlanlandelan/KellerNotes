package com.justdoit.keller.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 项目配置文件，从application.properties中加载
 * @author yangkaile
 * @date 2019-05-30 09:38:05
 */
@Configuration
@PropertySource("classpath:application.properties")
public class CommonConfig {
    @Value("${server.port:8080}")
    public String port;

    @Value("${server.address:http://127.0.0.1}")
    public String address;

}
