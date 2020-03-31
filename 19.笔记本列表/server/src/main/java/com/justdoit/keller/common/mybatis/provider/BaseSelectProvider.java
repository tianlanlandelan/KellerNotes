package com.justdoit.keller.common.mybatis.provider;


import com.justdoit.keller.common.mybatis.BaseEntity;
import com.justdoit.keller.common.mybatis.SqlFieldReader;
import com.justdoit.keller.common.util.Console;
import com.justdoit.keller.common.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基础的 Mybatis SQL 提供类
 * @author yangkaile
 * @date 2019-07-12 15:44:55
 *
 */
public class BaseSelectProvider {

    public static Map<String,String> selectPrefixWithDetailedMap = new ConcurrentHashMap<>(16);
    public static Map<String,String> selectPrefixMap = new ConcurrentHashMap<>(16);


    /**
     * 根据ID 查询数据
     * @param entity 实体对象
     * @param <T> 实体类型
     * @return SELECT id,name... FROM route WHERE id = #{id}
     */
    public static <T extends BaseEntity> String selectById(T entity){
        String sql = getSelectPrefix(entity) + " WHERE id = #{id}";
        Console.info("selectById",sql,entity);
        return sql;
    }

    /**
     * 根据主键查询数据，要求至少有一个主键，且主键必须有值
     * @param entity
     * @param <T>
     * @return
     */
    public static <T extends BaseEntity> String selectByKey(T entity){
        try {
            String sql = getSelectPrefix(entity) + SqlFieldReader.getConditionByKeySuffix(entity);
            Console.info("selectByKey",sql,entity);
            return sql;
        }catch (Exception e){
            return null;
        }
    }


    /**
     * 查询所有数据，不带条件
     * @param entity 实体对象
     * @param <T> 实体类型
     * @return SELECT id,name... FROM router
     */
    public static <T extends BaseEntity> String selectAll(T entity){
        String sql = getSelectPrefix(entity);
        Console.info("selectAll",sql,entity);
        return sql;
    }


    /**
     *
     * 带条件的查询，该查询为动态查询，不可缓存
     * 传入的对象中带@IndexAttribute注解的字段有值的都作为查询条件
     * 传入对象中带@SortAttribute注解的字段作为排序字段
     * @param entity 实体对象
     * param and 多个查询条件组合方式 null:不指定查询条件  true:多个查询条件用AND连接  false:多个查询条件用OR连接
     * param asc 排序方式  null:不指定排序方式  true:按指定排序字段升序   false:按指定排序字段降序
     * @param <T> 实体类型
     * @return SELECT id,name... FROM router  WHERE name = #{name} AND serviceName = #{serviceName}  ORDER BY createTime ASC
     */
    public static <T extends BaseEntity> String selectByCondition(T entity){
        String sql = getSelectPrefix(entity)
                + SqlFieldReader.getConditionSuffix(entity)
                + SqlFieldReader.getSortSuffix(entity);
        Console.info("selectByCondition",sql,entity);
        return  sql;
    }

    /**
     * 查询记录总数
     * @param entity
     * @param <T>
     * @return SELECT COUNT(1) FROM router
     */
    public static <T extends BaseEntity> String selectCount(T entity){
        String sql = "SELECT COUNT(1) FROM " + SqlFieldReader.getTableName(entity);
        Console.info("selectCount",sql,entity);
        return sql;
    }

    /**
     * 根据条件查询记录总数
     * 传入的对象中带@IndexAttribute注解的字段有值的都作为查询条件
     * @param entity
     * param and 多个查询条件组合方式 null:不指定查询条件  true:多个查询条件用AND连接  false:多个查询条件用OR连接
     * @param <T>
     * @return SELECT COUNT(1) FROM router WHERE name = #{name} AND serviceName = #{serviceName}
     */
    public static <T extends BaseEntity> String selectCountByCondition(T entity){
        String sql = selectCount(entity) + SqlFieldReader.getConditionSuffix(entity);
        Console.info("selectCountByCondition",sql,entity);
        return sql;
    }

    /**
     * 不加条件的分页查询
     * @param entity 实体对象
     * param startRows 起始行
     * param pageSize 查询页大小
     * @param <T> 实体类型
     * @return  SELECT id,name... FROM router  LIMIT #{startRows},#{pageSize}
     */
    public static <T extends BaseEntity> String selectPageList(T entity){
        String sql = selectAll(entity) + " LIMIT #{baseKyleStartRows},#{baseKylePageSize}";
        Console.info("selectPageList",sql,entity);
        return sql;
    }

    /**
     * 加条件的分页查询
     * 传入的对象中带@IndexAttribute注解的字段有值的都作为查询条件
     * @param entity
     * param and 多个查询条件组合方式 null:不指定查询条件  true:多个查询条件用AND连接  false:多个查询条件用OR连接
     * param asc 排序方式  null:不指定排序方式  true:按指定排序字段升序   false:按指定排序字段降序
     * param startRows 起始行数
     * param pageSize 查询条数
     * @param <T>
     * @return SELECT id,name... FROM router  WHERE name = #{name} AND serviceName = #{serviceName}  ORDER BY createTime ASC LIMIT #{startRows},#{pageSize}
     */
    public static <T extends BaseEntity> String selectPageListByCondition(T entity){
        String sql = selectByCondition(entity) + " LIMIT #{baseKyleStartRows},#{baseKylePageSize}";
        Console.info("selectPageListByCondition",sql,entity);
        return sql;
    }

    /**
     * 获取通用查询前缀
     * @param entity 实体类类型
     * @return SELECT 所有字段 FROM 表名
     */
    private static <T extends BaseEntity> String getSelectPrefix(T entity){
        String className = entity.getClass().getName();
        String sql;
        if(entity.isBaseKyleDetailed()){
            sql = selectPrefixWithDetailedMap.get(className);
        }else {
            sql = selectPrefixMap.get(className);
        }
        if(StringUtils.isEmpty(sql)){
            sql = "SELECT " + SqlFieldReader.getFieldStr(entity) + " FROM " + SqlFieldReader.getTableName(entity) + " ";
            if(entity.isBaseKyleDetailed()){
                selectPrefixWithDetailedMap.put(className,sql);
            }else {
                selectPrefixMap.put(className,sql);
            }
        }
        return sql;

    }
}
