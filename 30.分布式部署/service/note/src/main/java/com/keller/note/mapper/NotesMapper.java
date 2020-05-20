package com.keller.note.mapper;

import com.keller.common.mybatis.BaseMapper;
import com.keller.note.entity.NotesInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @date 2020-03-31 15:17:25
 * @author yangkaile
 *
 */
@Mapper
public interface NotesMapper extends BaseMapper<NotesInfo> {
}
