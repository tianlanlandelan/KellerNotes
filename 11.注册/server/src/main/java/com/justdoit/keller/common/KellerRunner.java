package com.justdoit.keller.common;

import com.justdoit.keller.common.config.CommonConfig;
import com.justdoit.keller.common.util.RequestUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 继承Application接口后项目启动时会按照执行顺序执行run方法
 * 通过设置Order的value来指定执行的顺序
 */
@Component
@Order(value = 1)
public class KellerRunner implements ApplicationRunner {
    @Resource
    private CommonConfig config;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        RequestUtil.port = config.port;
        RequestUtil.address = config.address;
    }
}
