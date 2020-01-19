package com.justdoit.keller.mapper;

import com.justdoit.keller.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user_info")
    List<UserInfo> selectAll();

}
