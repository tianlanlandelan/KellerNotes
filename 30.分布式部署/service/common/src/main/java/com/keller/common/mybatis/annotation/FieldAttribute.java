package com.keller.common.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段名注解，用于要查询的字段名之前
 * 在select * from tableName时,代替*的位置
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldAttribute {
    /**
     * 字段说明
     * @return
     */
    String value() default "";

    /**
     * 是否必填字段，默认不是必填
     * @return
     */
    boolean notNull() default false;

    /**
     * 字段长度 ，仅可变长类型设置
     * String 、byte[] 类型分别对应 mysql 中 varchar、varbinary类型，需要设置长度，默认50
     * @return
     */
    int length() default 0;

    /**
     * 是否唯一，默认不唯一
     * @return
     */
    boolean isUnique() default false;

    /**
     * 是否是明细字段，如果是明细字段，在查询列表时不显示该字段
     * @return
     */
    boolean isDetailed() default false;

    /**
     * 是否是索引字段，如果是索引字段，在建表时会以该字段创建索引，在查询时，字段有值会作为查询条件使用
     * @return
     */
    boolean isIndex() default false;

    /**
     * 是否要作为查询条件，如果作为查询条件，该字段若有值，会作为查询条件使用
     * @return
     */
    boolean isCondition() default false;
}
