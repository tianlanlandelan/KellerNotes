package com.justdoit.keller.mybatis;


import com.justdoit.keller.mybatis.annotation.*;
import com.justdoit.keller.util.Console;
import com.justdoit.keller.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Provider工具类
 * 提供获取读取表名、字段名等公用方法
 * @author yangkaile
 * @date 2019-09-12 15:29:19
 */
public class SqlFieldReader {



    /**
     * 读取表名，要求类上有@TableAttribute注解
     * @param entity 实体对象
     * @return tableName
     */
    public static <T extends BaseEntity> String getTableName(T entity){
        TableAttribute table = entity.getClass().getAnnotation(TableAttribute.class);
        if(table == null){
            return null;
        }
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
            if(field.getAnnotation(FieldAttribute.class) != null){
                FieldAttribute fieldAttribute = field.getAnnotation(FieldAttribute.class);
                //如果查询明细字段，返回明细字段
                if(entity.isBaseKyleDetailed()){
                    builder.append(field.getName()).append(",");
                //如果不查询明细字段，不返回明细字段
                }else {
                    if(!fieldAttribute.detailed()){
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
     * 传入的对象中带@IndexAttribute注解的字段有值的都作为查询条件
     * @param entity 实体对象
     * @param <T> 实体类型
     * @return WHERE name = #{name} OR controllerName = #{controllerName}
     */
    public static <T extends BaseEntity> String getConditionSuffix(T entity){
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
            for(Field field:fields){
                if(field.getAnnotation(IndexAttribute.class) != null){
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
    public static <T> String getConditionByKeySuffix(T entity) throws BaseException {
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
     * @param cls 实体类型
     * @return {id,name}
     */
    public static List<String> getFields(Class cls){
        Field[] fields = cls.getDeclaredFields();
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
     * 读取@KeyAttribute注解和@AutoIncrKeyAttribute注解，解析主键
     * 读取@IndexAttribute注解，解析索引
     *
     * 创建的数据表，含表名、数据表描述、字段名、字段描述、主键、自增主键、索引
     * @param entity
     * @return
     */
    public static <T extends BaseEntity> String getCreateTableSql(T entity){
        Class cls = entity.getClass();
        TableAttribute table =  entity.getClass().getAnnotation(TableAttribute.class);
        if(table == null){
            return null;
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

        List<SqlField> fieldList = getFieldAnnotationList(cls);
        /*
        解析字段描述：是否唯一、是否必填、是否设置了最大长度等
         */
        for(SqlField field : fieldList){
            builder.append(field.getName())
                    .append(" ")
                    .append(TypeCaster.getType(field.getType(),field.getLength()));
            if(field.isNotNull()){
                builder.append(" not null ");
            }

            if(field.isUnique()){
                builder.append(" unique ");
            }

            //如果有字段说明，添加字段说明
            if(StringUtils.isNotEmpty(field.getComment())) {
               builder.append(" comment '")
                       .append(field.getComment())
                       .append("'");
            }
            builder.append(", \n");
        }
        builder.deleteCharAt(builder.lastIndexOf(","));
        builder.append(") ");

        // 如果有表说明，添加表说明
        if(StringUtils.isNotEmpty(tableComment)){
            builder.append("comment '")
                    .append(tableComment)
                    .append("'; \n");
        }else {
            builder.append("; \n");
        }


        // 设置主键
        SqlField key = SqlFieldReader.getKey(cls);
        SqlField autoIncrKey = SqlFieldReader.getAutoIncrKey(cls);

        if(key != null && autoIncrKey != null){
            builder .append("alter table ")
                    .append(tableName)
                    .append(" change ")
                    .append(autoIncrKey.getName())
                    .append(" ")
                    .append(autoIncrKey.getName())
                    .append(" ")
                    .append(TypeCaster.getType(autoIncrKey.getType(),autoIncrKey.getLength()))
                    .append(" auto_increment primary key ; \n");
        }else if(key != null){
            builder.append("alter table ")
                    .append(tableName)
                    .append(" add primary key (")
                    .append(key.getName())
                    .append("); \n");
        }

        //设置索引
        List<SqlField> indexMap = SqlFieldReader.getIndexList(cls);
        for(SqlField field:indexMap){
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
        Console.print("",builder.toString());
        return builder.toString();
    }


    /**
     *
     * @param cls           实体类 Class
     * @param annotation    注解 Class
     * @return
     */
    public static List<SqlField> getAnnotationList(Class cls, Class annotation){
        Field[] fields = cls.getDeclaredFields();
        List<SqlField> list = new ArrayList<>();
        for(Field field:fields){
            if(annotation != null && field.getAnnotation(annotation) != null){
                list.add(new SqlField(field.getName(), field.getType().getSimpleName()));
            }
        }
        return list;
    }

    /**
     * 获取字段列表
     * @param cls
     * @return
     */
    public static List<SqlField> getFieldAnnotationList(Class cls){
        Field[] fields = cls.getDeclaredFields();
        List<SqlField> list = new ArrayList<>();

        for(Field field:fields){
            FieldAttribute fieldAttribute = field.getAnnotation(FieldAttribute.class);
            if(fieldAttribute != null){
                SqlField sqlField = new SqlField(field.getName(),field.getType().getSimpleName());
                sqlField.setComment(fieldAttribute.value());
                sqlField.setNotNull(fieldAttribute.notNull());
                sqlField.setLength(fieldAttribute.length());
                sqlField.setUnique(fieldAttribute.unique());
                list.add(sqlField);
            }
        }
        return list;
    }

    /**
     * 获取索引字段列表
     * @param cls
     * @return
     */
    public static List<SqlField> getIndexList(Class cls){
        return getAnnotationList(cls,IndexAttribute.class);
    }

    /**
     * 获取主键，默认id字段是主键
     * 如果没有设置主键时，将id字段设为主键
     * 如果设置了多个主键，只取第一个
     * @param cls
     * @return
     */
    public static SqlField getKey(Class cls){
        List<SqlField> list = getAnnotationList(cls,KeyAttribute.class);
        if(list.size() == 0){
            Field[] fields = cls.getDeclaredFields();
            for(Field field:fields){
                if("id".equals(field.getName())){
                    return new SqlField(field.getName(),field.getType().getSimpleName());
                }
            }
        }else {
            return list.get(0);
        }
        return null;
    }

    /**
     * 获取自增主键
     * @param cls
     * @return
     */
    public static SqlField getAutoIncrKey(Class cls){
        List<SqlField> list = getAnnotationList(cls,AutoIncrKeyAttribute.class);
        if(list.size() == 0){
            return null;
        }else {
            return list.get(0);
        }
    }

    /**
     * 判断一个对象的指定字段有没有值
     * @param entity 实体对象
     * @param fieldName 对象的字段名
     * @param <T> 实体类型
     * @return 值存在且不为null:返回true; 否则:返回false
     */
    public static <T> boolean hasValue(T entity,String fieldName){
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
