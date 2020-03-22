package com.justdoit.keller;

public class Integer1 extends Number{

    //Integer的值
    private static int value;

    /**
     * 构造方法用来 new创建对象
     * @param v
     */
    public Integer1 (int v){
        this.value=v;
    }


    /**
     * 静态方法，用来直接=赋值( 可是就是不管用。求解决)
     * @param value
     * @return
     */
    public static Integer1 valueOf(int value){
        return new Integer1(value);
    }
    public int intValue(){
        return value ++;
    }


    public long longValue(){
        return (long) value++;
    }

    public float floatValue(){
        return (float) value++;
    }


    public  double doubleValue(){
        return (double) value ++;
    }
}
