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

    /**
     * 项目运行端口
     */
    @Value("${server.port:8080}")
    public String port;

    /**
     * 项目运行的IP地址
     */
    @Value("${server.address:http://127.0.0.1}")
    public String address;

    /**
     * 应用名
     */
    @Value(("${spring.application.name:KellerNote}"))
    public String appName;

    /**
     * 邮件服务器地址
     */
    @Value("${mail.server.host}")
    public  String mailServerHost;
    /**
     * 发件人名称
     */
    @Value("${mail.server.user}")
    public  String mailServerUser;
    /**
     * 发件人密码
     */
    @Value("${mail.server.password}")
    public  String mailServerPassword;



}
