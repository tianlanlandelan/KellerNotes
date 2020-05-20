package com.keller.user.controller;

import com.keller.common.http.Response;
import com.keller.common.util.ErrorLogUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


/**
 * @author yangkaile
 * @date 2020-05-09 10:32:01
 * 处理全局异常
 */
@ControllerAdvice
public class ErrorController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity errorHandler(Exception ex) {
        ErrorLogUtils.getInstance(rabbitTemplate).sendErrorLog(ex);
        return Response.error();
    }
}
