package com.justdoit.keller.mapper;

import com.justdoit.keller.entity.UserInfo;
import com.justdoit.keller.common.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<UserInfo> {
}
