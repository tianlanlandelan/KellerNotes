package com.justdoit.keller.mapper;

import com.justdoit.keller.common.mybatis.BaseMapper;
import com.justdoit.keller.entity.EmailLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmailMapper extends BaseMapper<EmailLog> {
}
