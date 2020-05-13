package com.justdoit.keller.mapper;

import com.justdoit.keller.common.mybatis.BaseMapper;
import com.justdoit.keller.entity.RegisterLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 *
 * @author yangkaile
 * @date 2020-04-28 09:25:02
 */
@Mapper
public interface RegisterLogMapper extends BaseMapper<RegisterLog> {
    String tableName = "register_log";
    String whereCreateTimeBetween =
            " WHERE createTime BETWEEN STR_TO_DATE(#{param1},'%Y-%m-%d') " +
                    " AND STR_TO_DATE(#{param2},'%Y-%m-%d') " +
                    " GROUP BY time " +
                    " ORDER BY time ";
    /**
     *  获取日增长量
     * @param beginDate 开始日期 格式 YYYY-mm-DD
     * @param endDate   截止日期 格式 YYYY-mm-DD，如 2020-04-05,会截止到 2020-04-05 00:00:00
     * @return
     */
    @Select("SELECT DATE_FORMAT(createTime,'%Y-%m-%d') as time,count(1) count " +
            " FROM " + tableName + whereCreateTimeBetween )
    List<Map<String,Integer>> getCountByDay(String beginDate, String endDate);

    /**
     *  获取月增长量
     * @param beginDate 开始日期 格式 YYYY-mm-DD
     * @param endDate   截止日期 格式 YYYY-mm-DD，如 2020-04-05,会截止到 2020-04-05 00:00:00
     * @return
     */
    @Select("SELECT DATE_FORMAT(createTime,'%Y-%m') as time,count(1) count " +
            " FROM " + tableName + whereCreateTimeBetween)
    List<Map<String,Integer>> getCountByMonth(String beginDate, String endDate);
}
