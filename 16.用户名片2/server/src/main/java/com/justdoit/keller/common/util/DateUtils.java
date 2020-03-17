package com.justdoit.keller.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yangkaile
 * @date 2019-05-30 08:55:13
 */
public class DateUtils {
    /**
     * 获取时间字符串
     * @param date
     * @return
     */
    public static String getDate(Date date){
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }
    public static String getDate(){
        return getDate(new Date());
    }

    /**
     * 获取日期时间字符串
     * @param date
     * @return
     */
    public static String getDateTime(Date date){
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static String getDateTime(){
        return getDateTime(new Date());
    }

    public static String getTimeMaskSecond(){
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }


    /**
     * 获取时间标识，年月日时分秒毫秒，用于文件名
     * @param date
     * @return
     */
    public static String getTimeMask(Date date){
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return simpleDateFormat.format(date);
    }

    public static String getTimeMask(){
        return getTimeMask(new Date());
    }

    public static void main(String[] args){
        Console.print("getDate",DateUtils.getDate());
        Console.print("getDateTime",DateUtils.getDateTime());
        Console.print("getTimeMask",DateUtils.getTimeMask());
    }
}
