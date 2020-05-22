package com.keller.proxy.config;

import com.keller.common.config.MqConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  RabbitMQ 队列名
 * @author yangkaile
 * @date  2020-05-21 16:17:06
 */
@Configuration
public class RabbitConfig {
    /**
     * 邮件发送日志队列
     * @return
     */
    @Bean
    public Queue emailLog(){
        return new Queue(MqConstants.EMAIL_LOG);
    }

    /**
     * 用户登录日志队列
     * @return
     */
    @Bean
    public Queue logonLog(){
        return new Queue(MqConstants.LOGON_LOG);
    }

    /**
     * 用户注册日志队列
     * @return
     */
    @Bean
    public Queue registerLog(){
        return new Queue(MqConstants.REGISTER_LOG);
    }


    /**
     * 异常日志队列
     * @return
     */
    @Bean
    public Queue errorLog(){
        return new Queue(MqConstants.ERROR_LOG);
    }
}
