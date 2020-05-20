package com.keller.note.mapper;

import com.keller.common.mybatis.BaseMapper;
import com.keller.note.entity.NoteInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoteMapper extends BaseMapper<NoteInfo> {
}
