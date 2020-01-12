package com.justdoit.keller.mapper;

import com.justdoit.keller.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 注释1：用 @Mapper 注解将 UserMapper 接口标注为一个映射接口，交给 Spring 管理
 */
@Mapper
public interface UserMapper {
    /**
     * 注释2：用 @Select 注解为 selectAll() 方法配置一条查询语句 “select * from user_info”，
     * 并将该查询语句的结果映射为 List<UserInfo> 对象
     * @return List<UserInfo>
     */
    @Select("select * from user_info")
    List<UserInfo> selectAll();

}
