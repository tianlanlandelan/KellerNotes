package com.keller.log.mapper;

import com.keller.common.entity.ErrorLog;
import com.keller.common.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ErrorLogMapper extends BaseMapper<ErrorLog> {
}
