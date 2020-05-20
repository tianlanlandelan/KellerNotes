package com.keller.common.util;

public class ObjectUtils {
    /**
     * 只要有一个为空的，就返回 true
     * 判断一系列字符串中是否有空的（包含:空字符串、null、纯空格字符）
     * @param parameters 需要判断的字符串，可以是多个
     * @return
     */
    public static boolean isEmpty(Object... parameters){
        for(Object param:parameters){
            if(param == null){
                return true;
            }
            if(param instanceof String){
                String str = (String) param;
                if(str.isEmpty()){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 仅当所有元素都有值才返回 true
     * 判断一组对象中没有空值，仅当所有参数都有值时才会返回 true
     * @param parameters
     * @return
     */
    public static boolean noEmpty(Object... parameters){
        return !isEmpty(parameters);
    }

    /**
     * 只要有一个元素有值，就返回 true
     * 判断一组元素是否有值
     * @param parameters
     * @return
     */
    public static boolean hasValue(Object... parameters){
        for(Object param:parameters){
            if(param != null){
                if(param instanceof String){
                    String str = (String)param;
                    return !str.isEmpty();
                }else{
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * 仅当所有元素都没值才返回true
     * @param parameters
     * @return
     */
    public static boolean noValue(Object... parameters){
        return !hasValue(parameters);
    }
}
