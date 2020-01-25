package com.justdoit.keller.mapper;

import com.justdoit.keller.entity.UserInfo;
import com.justdoit.keller.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<UserInfo> {
    @Select("select id, type, email, password, createTime, status, isDelete, updateTime, updateUserId " +
            "from user_info")
    List<UserInfo> selectAll();

    @Select("select id, type, email, password, createTime, status, isDelete, updateTime, updateUserId " +
            "from user_info where id = #{id}")
    UserInfo selectById(int id);

    @Select("Select id, type, email, password, createTime, status, isDelete, updateTime, updateUserId " +
            "from user_info where email = #{email} ")
    UserInfo selectByEmail(String email);

    @Insert("Insert into user_info (id, type, email, password, createTime, status, isDelete) " +
            "values(#{id}, #{type}, #{email}, #{password}, #{createTime}, #{status}, #{isDelete})")
    @Options(useGeneratedKeys=true,keyProperty = "id", keyColumn = "id")
    Integer insertAndReturnKey(UserInfo userInfo);

}
