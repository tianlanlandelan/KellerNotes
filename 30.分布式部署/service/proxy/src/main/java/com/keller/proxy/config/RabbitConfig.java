package com.keller.proxy.config;

import com.keller.common.config.MqConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
public class RabbitConfig {

    @Bean
    public Queue emailLog(){
        return new Queue(MqConstants.EMAIL_LOG);
    }
    @Bean
    public Queue logonLog(){
        return new Queue(MqConstants.LOGON_LOG);
    }
    @Bean
    public Queue registerLog(){
        return new Queue(MqConstants.REGISTER_LOG);
    }
    @Bean
    public Queue errorLog(){
        return new Queue(MqConstants.ERROR_LOG);
    }
}
