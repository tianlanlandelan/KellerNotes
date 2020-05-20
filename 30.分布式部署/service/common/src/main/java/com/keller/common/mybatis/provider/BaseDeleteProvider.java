package com.keller.common.mybatis.provider;



import com.keller.common.mybatis.BaseEntity;
import com.keller.common.mybatis.SqlFieldReader;
import com.keller.common.util.Console;
import com.keller.common.util.ObjectUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yangkaile
 * @date 2019-07-18 09:56:43
 */
public class BaseDeleteProvider {

    public static Map<String,String> deleteByIdMap = new ConcurrentHashMap<>(16);

    /**
     * 根据Id 删除数据，要求必须有id字段
     * @param entity
     * @param <T>
     * @return DELETE FROM router  WHERE id = #{id}
     */
    public static <T extends BaseEntity> String deleteById(T entity){
        Class cls = entity.getClass();
        String className = cls.getName();
        String sql = deleteByIdMap.get(className);
        if(ObjectUtils.isEmpty(sql)){
            sql = getDeletePrefix(entity) + " WHERE id = #{id} ";
            deleteByIdMap.put(className,sql);
        }
        Console.info("deleteById",sql,entity);
        return sql;
    }

    public static <T extends BaseEntity> String deleteByKey(T entity){
        try {
            String sql =  getDeletePrefix(entity) + SqlFieldReader.getConditionByKeySuffix(entity);
            Console.info("deleteByKey",sql,entity);
            return sql;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 根据条件删除，该查询为动态查询，不可缓存
     * 传入的对象中带@IndexAttribute注解的字段有值的都作为查询条件
     * 多个查询条件用And连接
     * @param entity 实体对象
     * param and 多个查询条件组合方式 null:不指定查询条件  true:多个查询条件用AND连接  false:多个查询条件用OR连接
     * @param <T> 对象类型
     * @return DELETE FROM router  WHERE name = #{name} AND serviceName = #{serviceName}
     */
    public static <T extends BaseEntity> String deleteByCondition(T entity){
        String sql = getDeletePrefix(entity) + SqlFieldReader.getConditionSuffix(entity);
        Console.info("deleteByCondition",sql,entity);
        return sql;
    }

    private static <T extends BaseEntity> String getDeletePrefix(T entity){
        return "DELETE FROM " + SqlFieldReader.getTableName(entity) + " ";
    }

    public static void main(String[] args){

    }

}
