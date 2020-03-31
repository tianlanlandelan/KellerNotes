package com.justdoit.keller;

import com.justdoit.keller.common.util.RestTemplateUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class KellerApplication {

    @Bean
    /**
     * 引入RestTemplate Bean
     * 用来进行服务间的Http通信
     * 同时重新定义其解析时用到的字符集，防止中文乱码
     */
    RestTemplate restTemplate(){
        return RestTemplateUtils.getHttpsTemplate();

    }
    public static void main(String[] args) {
        SpringApplication.run(KellerApplication.class, args);
    }

}
