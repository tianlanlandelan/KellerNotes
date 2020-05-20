package com.keller.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间处理工具类，获取指定格式的日期字符串
 * @author yangkaile
 * @date 2019-05-30 08:55:13
 */
public class DateUtils {

    private static SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 获取时间字符串
     * @param date
     * @return
     */
    public static String getDate(Date date){
        return dateFormat.format(date);
    }

    public static String getNextDay(String dateStr){
        try{
            Date date = dateFormat.parse(dateStr);
            long day = 24 * 60 * 60 * 1000;
            return dateFormat.format(new Date(date.getTime() + day));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getNextMonth(String dateStr){
        try{
            Date date = dateFormat.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH,1);
            date = calendar.getTime();
            return dateFormat.format(date);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
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
        Console.print("getNextDay",DateUtils.getNextDay("2020-04-30"));
        Console.print("getNextMonth",DateUtils.getNextMonth("2020-04-29"));

    }
}
