package com.keller.common.mybatis;

import com.keller.common.util.Console;
import com.keller.common.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Java类型和mysql类型间的转换，仅创建数据表时使用
 * @author yangkaile
 * @date 2019-09-12 09:08:43
 */
public class TypeCaster {
    /**
     * varchar/varbinary类型，允许最大长度为65535，在这里限制：如果超过3000，转换为text/blob
     */
    private static final int MAX = 3000;

    /**
     * TINYTEXT 	256 bytes
     * TEXT 	65,535 bytes 	~64kb
     * MEDIUMTEXT 	 16,777,215 bytes 	~16Mb
     * LONGTEXT 	4,294,967,295 bytes 	~4Gb
     */
    private static final int TEXT_MAX = 65535;

    /**
     * decimal类型的最大长度为65，根据平时使用的需要，设置为20，足够大多数场景使用了
     */
    private static final int DECIMAL_MAX = 20;
    private static Map<String,String> map = new HashMap<>(16);

    private static final String STRING = "string";
    private static final String INT = "int";
    private static final String INTEGER = "integer";
    private static final String LONG = "long";
    private static final String DATE = "date";
    private static final String BYTE_ARRAY = "byte[]";
    private static final String FLOAT = "float";
    private static final String DOUBLE = "double";
    static {
        map.put(STRING,"varchar(50)");
        map.put(INT,"int");
        map.put(INTEGER,"int");
        map.put(LONG,"bigint");
        map.put(DATE,"datetime");
        map.put(BYTE_ARRAY,"varbinary(50)");
        map.put(FLOAT,"decimal(10,2)");
        map.put(DOUBLE,"decimal(10,2)");
    }
    /**
     * 根据Java数据类型和设置的长度，转换为MySQL的数据类型
     * @param key
     * @param length
     * @return
     */
    public static String getType(String key,int length){
        if(ObjectUtils.isEmpty(key)){
         return null;
        }

        if(length <= 0){
            return map.get(key.toLowerCase());
        }

        /*
        float/Float/double/Double类型判断设置的长度是否符合规则，如果超长，将长度设置为允许的最大长度
         */
        if(FLOAT.equalsIgnoreCase(key)
                || DOUBLE.equalsIgnoreCase(key)){
            length = length > DECIMAL_MAX ? DECIMAL_MAX:length;
            return "decimal(" + length + ",2)";
        }

        //String 根据长度，转换为 varchar 或 text
        if(STRING.equalsIgnoreCase(key)){
            if(length < MAX){
                return "varchar(" + length + ")";
            }
            if(length < TEXT_MAX){
                return "text";
            }

            return "mediumtext";


        }

        //byte[] 根据长度，转换为 varbinary 或 blob
        if(BYTE_ARRAY.equalsIgnoreCase(key)){
            if(length < MAX){
                return "varbinary(" + length + ")";
            }
            return "blob";
        }

        return map.get(key.toLowerCase());
    }

    public static void main(String[] args){
        Console.println("String",getType("String",10000));
        Console.println("Integer",getType("Integer",100));
        Console.println("float",getType("float",100));
        Console.println("Float",getType("Float",10));
        Console.println("long",getType("long",10));
        Console.println("Long",getType("Long",10));
        Console.println("Date",getType("Date",10));
        Console.println("double",getType("double",10));
    }
}
