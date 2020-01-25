package com.justdoit.keller.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自增主键
 * 在创建数据表时使用
 * @author yangkaile
 * @date 2019-09-12 13:41:18
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoIncrKeyAttribute {
}
