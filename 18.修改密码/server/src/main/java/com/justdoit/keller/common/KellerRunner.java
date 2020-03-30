package com.justdoit.keller.common;

import com.justdoit.keller.common.config.CommonConfig;
import com.justdoit.keller.common.config.PublicConstant;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author yangkaile
 * @date 2020-02-26 16:38:55
 * 继承Application接口后项目启动时会按照执行顺序执行run方法
 * 通过设置Order的value来指定执行的顺序
 */
@Component
@Order(value = 1)
public class KellerRunner implements ApplicationRunner {
    @Resource
    private CommonConfig config;

    @Override
    public void run(ApplicationArguments args) {
        PublicConstant.port = config.port;
        PublicConstant.serviceUrl = "https://127.0.0.1:" + config.port;
        PublicConstant.appName = config.appName;
        PublicConstant.mailServerHost = config.mailServerHost;
        PublicConstant.mailServerUser = config.mailServerUser;
        PublicConstant.mailServerPassword = config.mailServerPassword;

        PublicConstant.nginxPath = config.nginxPath;
        PublicConstant.nginxUrl = config.nginxUrl;
        PublicConstant.imgPath = config.imgPath;
        PublicConstant.thumPath = config.thumPath;
        PublicConstant.webUrl = config.webUrl;
    }
}
