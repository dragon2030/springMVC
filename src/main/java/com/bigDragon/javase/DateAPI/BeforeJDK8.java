package com.bigDragon.javase.DateAPI;

/**
 * jdk 8之前日期和时间的API测试
 *
 * @author bigDragon
 * @create 2020-12-31 16:12
 */
public class BeforeJDK8 {
    public static void main(String[] args) {
        BeforeJDK8 beforeJDK8 = new BeforeJDK8();
    }
    public void test1(){
        //System类中currentTimeMillis():返回当前时间与1970年1月1日0时0分0秒之间以毫秒为单位的时间差
        //称为时间戳
        long time = System.currentTimeMillis();
        System.out.println(time);
    }
}
