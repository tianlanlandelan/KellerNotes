package com.keller.common.mybatis;

/**
 * @author yangkaile
 * @date 2019-07-18 09:49:17
 * 自定义异常，用来处理BaseMapper使用过程中抛出的异常
 */
public class BaseException extends RuntimeException{
    public BaseException(String message){
        super(message);
    }
}
