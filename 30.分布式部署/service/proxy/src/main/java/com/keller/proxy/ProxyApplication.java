package com.keller.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yangkaile
 * @date 2020-05-19 08:25:56
 * 代理服务
 *  提供到各组件的代理，同时负责文件上传功能
 */
@SpringBootApplication
public class ProxyApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProxyApplication.class, args);
    }

}
