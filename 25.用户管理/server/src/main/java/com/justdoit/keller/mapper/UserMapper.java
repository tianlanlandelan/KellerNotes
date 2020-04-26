package com.justdoit.keller.mapper;

import com.justdoit.keller.entity.UserInfo;
import com.justdoit.keller.common.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserInfo> {

    /**
     * 自定义查询条件：按邮箱模糊查询
     * 按照MyBatis 的 SQL 格式书写
     * 该条件通过baseEntity.setBaseKyleCustomCondition设置
     */
    String whereEmailLike = "email  LIKE CONCAT('%',#{email},'%') ";
}
