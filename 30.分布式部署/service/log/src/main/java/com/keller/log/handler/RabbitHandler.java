package com.keller.log.handler;

import com.alibaba.fastjson.JSON;
import com.keller.common.config.MqConstants;
import com.keller.common.entity.EmailLog;
import com.keller.common.entity.ErrorLog;
import com.keller.common.entity.LoginLog;
import com.keller.common.entity.RegisterLog;
import com.keller.common.util.Console;
import com.keller.log.mapper.EmailLogMapper;
import com.keller.log.mapper.ErrorLogMapper;
import com.keller.log.mapper.LoginLogMapper;
import com.keller.log.mapper.RegisterLogMapper;
import com.keller.log.service.EmailLogService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author yangkaile
 * @date 2020-05-19 15:11:33
 *
 */
@Component
public class RabbitHandler {

    @Resource
    private EmailLogMapper emailLogMapper;

    @RabbitListener(queues = MqConstants.EMAIL_LOG)
    public void emailLog(String content){
        Console.info("emailLog",content);
        EmailLog log = JSON.parseObject(content,EmailLog.class);
        emailLogMapper.baseInsertAndReturnKey(log);
    }

    @Resource
    private ErrorLogMapper errorLogMapper;

    @RabbitListener(queues = MqConstants.ERROR_LOG)
    public void errorLog(String content){
        Console.info("errorLog",content);
        ErrorLog log = JSON.parseObject(content,ErrorLog.class);
        errorLogMapper.baseInsertAndReturnKey(log);
    }

    @Resource
    private LoginLogMapper loginLogMapper;

    @RabbitListener(queues = MqConstants.LOGON_LOG)
    public void loginLog(String content){
        Console.info("loginLog",content);
        LoginLog log = JSON.parseObject(content,LoginLog.class);
        loginLogMapper.baseInsertAndReturnKey(log);
    }

    @Resource
    private RegisterLogMapper registerLogMapper;

    @RabbitListener(queues = MqConstants.REGISTER_LOG)
    public void registerLog(String content){
        Console.info("registerLog",content);
        RegisterLog log = JSON.parseObject(content,RegisterLog.class);
        registerLogMapper.baseInsertAndReturnKey(log);
    }
}
