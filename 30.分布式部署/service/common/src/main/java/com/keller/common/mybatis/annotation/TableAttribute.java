package com.keller.common.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表注解，用在entity的类声明之前
 * value赋值为表名
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableAttribute {
    /**
     * 表名
     * @return
     */
    String name() ;

    /**
     * 描述
     * @return
     */
    String comment() default "";
}
