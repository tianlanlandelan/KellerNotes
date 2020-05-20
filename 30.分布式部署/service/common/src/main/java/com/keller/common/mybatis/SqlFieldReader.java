package com.keller.common.mybatis;


import com.keller.common.mybatis.annotation.*;
import com.keller.common.util.Console;
import com.keller.common.util.ObjectUtils;
import com.keller.common.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Provider工具类
 * 提供获取读取表名、字段名等公用方法
 * @author yangkaile
 * @date 2019-09-12 15:29:19
 */
public class SqlFieldReader {

    public static ConcurrentHashMap<String,String> tableNameMap = new ConcurrentHashMap<>(16);

    /**
     * 读取表名，要求类上有@TableAttribute注解
     * @param entity 实体对象
     * @return tableName
     */
    public static <T extends BaseEntity> String getTableName(T entity) {
        Class cls = entity.getClass();
        String tableName = tableNameMap.get(cls.getName());
        if(ObjectUtils.noEmpty(tableName)){
            return tableName;
        }
        TableAttribute table = entity.getClass().getAnnotation(TableAttribute.class);
        if(table == null){
            throw new BaseException("需要解析表名，但未找到@TableAttribute注解");
        }
        tableNameMap.put(cls.getName(),table.name());
        return table.name();
    }

    /**
     * 将所有字段名以逗号拼接起来返回
     * 从属性前的@FieldAttribute注解解析要查询的字段名
     * 当所有属性都没有@FieldAttribute注解时，解析所有属性名作为字段名
     * @param entity 实体对象
     * @return id,name
     */
    public static <T extends BaseEntity> String getFieldStr(T entity){
        Class cls = entity.getClass();
        Field[] fields = cls.getDeclaredFields();
        //带@FieldAttribute注解的属性名
        StringBuilder builder = new StringBuilder();
        //所有属性名
        StringBuilder allFields = new StringBuilder();
        for(Field field:fields){
            allFields.append(field.getName()).append(",");
            FieldAttribute fieldAttribute = field.getAnnotation(FieldAttribute.class);
            if(fieldAttribute != null){
                //如果查询明细字段，返回明细字段
                if(entity.isBaseKyleDetailed()){
                    builder.append(field.getName()).append(",");
                //如果不查询明细字段，不返回明细字段
                }else {
                    if(!fieldAttribute.isDetailed()){
                        builder.append(field.getName()).append(",");
                    }
                }

            }
        }
        if(builder.length() > 0){
            return builder.substring(0,builder.length() - 1);
        }else if(allFields.length() > 0){
            return allFields.substring(0,allFields.length() - 1);
        }else {
            return  null;
        }
    }

    /**
     * 根据索引字段生成查询条件
     * 传入的对象中@FieldAttribute注解中 isCondition = true 的字段有值的都作为查询条件
     * @param entity 实体对象
     * @param <T> 实体类型
     * @return WHERE name = #{name} OR controllerName = #{controllerName}
     */
    public static <T extends BaseEntity> String getConditionSuffix(T entity){
        //如果设置了自定义的查询条件，返回自定义查询条件，不再判断字段值
        if(entity.getBaseKyleCustomCondition() != null){
            return " WHERE " + entity.getBaseKyleCustomCondition();
        }

        String condition;
        if(entity.getBaseKyleUseAnd() == null){
            return "";
        }
        if(entity.getBaseKyleUseAnd()){
            condition = "AND";
        }else {
            condition = "OR";
        }
        Class cls = entity.getClass();
                Field[] fields = cls.getDeclaredFields();
                StringBuilder builder = new StringBuilder();
                builder.append(" WHERE ");
                try {
                    FieldAttribute fieldAttribute;
                    for(Field field:fields){
                        fieldAttribute = field.getAnnotation(FieldAttribute.class);
                        if(fieldAttribute == null){
                            continue;
                        }
                //索引字段或设置为查询条件的字段中，有值的都作为查询条件
                if(fieldAttribute.isIndex() || fieldAttribute.isCondition()){
                    if(SqlFieldReader.hasValue(entity,field.getName())){
                        builder.append(field.getName())
                                .append(" = #{").append(field.getName()).append("} ")
                                .append(condition).append(" ");
                    }

                }
            }
            int index = builder.lastIndexOf(condition);
            if(index < 0){
                return "";
            }
            return builder.substring(0,index);

        }catch (Exception e){
            e.printStackTrace();
        }
        //注意，不要return null
        return "";

    }

    /**
     * 获取主键的查询条件
     * 传入的对象必须满足以下条件：
     * 1. 必须有且只有一个带有@keyAttribute注解的字段，如果有多个，只取第一个
     * 2. 带有@KeyAttribute注解的字段必须有值
     * 这是为了避免产生因为没有设置@KeyAttribute注解而造成全部数据修改或删除的问题
     * @param entity
     * @param <T>
     * @return WHERE userId = #{userId}
     */
    public static <T extends BaseEntity> String getConditionByKeySuffix(T entity) throws BaseException {
        Class cls = entity.getClass();
        Field[] fields = cls.getDeclaredFields();
        StringBuilder builder = new StringBuilder();
        builder.append(" WHERE ");
        try {
            for(Field field:fields){
                if(field.getAnnotation(KeyAttribute.class) != null){
                    if(hasValue(entity,field.getName())){
                        builder.append(field.getName())
                                .append(" = #{").append(field.getName()).append("} ");
                    }else {
                        throw new BaseException("@KeyAttribute修饰的字段不能为空");
                    }
                    break;
                }
            }
            int index = builder.lastIndexOf("=");
            if(index < 0){
                throw new BaseException("没有找到@KeyAttribute修饰的字段");
            }
            return builder.toString();
        }catch (Exception e){
            e.printStackTrace();
            throw new BaseException(e.getMessage());
        }
    }

    /**
     *
     * @param entity
     * @param <T>
     * @return
     */
    public static <T extends BaseEntity> String getSortSuffix(T entity){
        String condition;
        if(entity.getBaseKyleUseASC() == null){
            return "";
        }
        if(entity.getBaseKyleUseASC()){
            condition = "ASC";
        }else {
            condition = "DESC";
        }
        Class cls = entity.getClass();
        Field[] fields = cls.getDeclaredFields();
        StringBuilder builder = new StringBuilder();
        builder.append(" ORDER BY ");
        try {
            for(Field field:fields){
                if(field.getAnnotation(SortAttribute.class) != null){
                    builder.append(field.getName()).append(" ")
                            .append(condition).append(",");

                }
            }
            int index = builder.lastIndexOf(",");
            if(index < 0){
                return "";
            }
            return builder.substring(0,index);

        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取所有字段列表
     * 读取类中带@FieldAttribute注解的字段，如果都没有带该注解，则返回类中所有字段
     * @return {id,name}
     */
    public static <T extends BaseEntity> List<String> getFields(T entity){
        Field[] fields = entity.getClass().getDeclaredFields();
        List<String> fieldList = new ArrayList<>();
        List<String> allList = new ArrayList<>();
        //带@FieldAttribute注解的属性名
        for(Field field:fields){
            allList.add(field.getName());
            if(field.getAnnotation(FieldAttribute.class) != null){
                fieldList.add(field.getName());
            }
        }
        if(fieldList.size() == 0){
            return allList;
        }
        return fieldList;
    }

    /**
     * 读取@TableAttribute注解，解析表名和描述
     * 读取@FieldAttribute注解，解析字段名和描述
     * 读取@KeyAttribute注解，解析主键
     * 读取@IndexAttribute注解，解析索引
     *
     * 创建的数据表，含表名、数据表描述、字段名、字段描述、主键、自增主键、索引
     * @param entity
     * @return
     */
    public static <T extends BaseEntity> String getCreateTableSql(T entity){
        TableAttribute table =  entity.getClass().getAnnotation(TableAttribute.class);
        if(table == null){
            throw new BaseException("要解析表名，未发现@TableAttribute注解");
        }
        String tableName = table.name();
        String tableComment = table.comment();

        StringBuilder builder = new StringBuilder();

        /*
         * 拼写基础建表语句
         */
        builder.append("create table ")
                .append(tableName)
                .append("( \n");


        // 添加字段
        builder.append(getAddFieldSql(entity));
        builder.append(") ");

        // 如果有表说明，添加表说明
        if(ObjectUtils.noEmpty(tableComment)){
            builder.append("comment '")
                    .append(tableComment)
                    .append("'; \n");
        }else {
            builder.append("; \n");
        }

        //添加主键
        builder.append(getCreateKeySql(entity));

        //添加索引
        builder.append(getCreateIndexSql(entity));

        Console.print("",builder.toString());
        return builder.toString();
    }


    public static <T extends BaseEntity> String getAddFieldSql(T entity){
        Field[] fields = entity.getClass().getDeclaredFields();
        StringBuilder builder = new StringBuilder();

        /*
        解析字段描述：是否唯一、是否必填、是否设置了最大长度等
         */
        for(Field field:fields){
            FieldAttribute fieldAttribute = field.getAnnotation(FieldAttribute.class);
            if(fieldAttribute != null){

                builder.append(field.getName())
                        .append(" ")
                        .append(TypeCaster.getType(field.getType().getSimpleName(),fieldAttribute.length()));
                if(fieldAttribute.notNull()){
                    builder.append(" not null ");
                }

                if(fieldAttribute.isUnique()){
                    builder.append(" isUnique ");
                }

                //如果有字段说明，添加字段说明
                if(ObjectUtils.noEmpty(fieldAttribute.value())) {
                    builder.append(" comment '")
                            .append(fieldAttribute.value())
                            .append("'");
                }
                builder.append(", \n");
            }
        }
        builder.deleteCharAt(builder.lastIndexOf(","));
        return builder.toString();
    }

    private static <T extends BaseEntity> String getCreateKeySql(T entity){
        Field[] fields = entity.getClass().getDeclaredFields();
        StringBuilder builder = new StringBuilder();
        for(Field field:fields){
            KeyAttribute keyAttribute = field.getAnnotation(KeyAttribute.class);
            if(keyAttribute != null){
                FieldAttribute fieldAttribute = field.getAnnotation(FieldAttribute.class);
                if(fieldAttribute == null){
                    return "";
                }
                builder .append("alter table ")
                        .append(getTableName(entity))
                        .append(" change ")
                        .append(field.getName())
                        .append(" ")
                        .append(field.getName())
                        .append(" ")
                        .append(TypeCaster.getType(field.getType().getSimpleName(),fieldAttribute.length()));
                if(keyAttribute.autoIncr()){
                    builder.append(" auto_increment ");
                }
                builder.append(" primary key comment '")
                        .append(fieldAttribute.value())
                        .append("'; \n");

                break;
            }
        }
        return builder.toString();
    }


    /**
     * 获取索引字段列表
     * @return
     */
    public static <T extends BaseEntity> String getCreateIndexSql(T entity){

        String tableName = getTableName(entity);
        StringBuilder builder = new StringBuilder();

        Field[] fields = entity.getClass().getDeclaredFields();
        for(Field field:fields){
            FieldAttribute fieldAttribute = field.getAnnotation(FieldAttribute.class);
            if(fieldAttribute != null && fieldAttribute.isIndex()){

                builder.append("alter table ")
                        .append(tableName)
                        .append(" add index ")
                        .append(tableName)
                        .append("_index_")
                        .append(field.getName())
                        .append(" (")
                        .append(field.getName())
                        .append("); \n");
            }
        }
        return builder.toString();
    }


    /**
     * 判断一个对象的指定字段有没有值
     * @param entity 实体对象
     * @param fieldName 对象的字段名
     * @param <T> 实体类型
     * @return 值存在且不为null:返回true; 否则:返回false
     */
    public static <T extends BaseEntity> boolean hasValue(T entity, String fieldName){
        try {
            Class cls = entity.getClass();
            Method method = cls.getMethod("get" + StringUtils.captureName(fieldName));
            if(method.invoke(entity) == null){
                return false;
            }else {
                return true;
            }
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }catch (NoSuchMethodException e){
            e.printStackTrace();
        }catch (InvocationTargetException e){
            e.printStackTrace();
        }
       return false;
    }
}
