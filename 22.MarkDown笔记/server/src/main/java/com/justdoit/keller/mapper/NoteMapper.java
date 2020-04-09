package com.justdoit.keller.mapper;

import com.justdoit.keller.common.mybatis.BaseMapper;
import com.justdoit.keller.entity.NoteInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoteMapper extends BaseMapper<NoteInfo> {
}
