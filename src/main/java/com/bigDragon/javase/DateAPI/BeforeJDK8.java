package com.bigDragon.javase.DateAPI;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * jdk 8之前日期和时间的API测试
 *
 * Date类
 * 有两个Date，其为子父类继承关系
 * java.util.Date类
 *      |---java.sql.Date类
 * 1.构造器的使用
 * 2.两个方法的使用
 *      >toString:显示年月日时分秒
 *      >getTime:获取当前Date对象对应的时间戳
 *
 * java.sql.Date对应着数据库中日期类型的变量
 *      >实例化
 *      >java.util.Date-->java.sql.Date的转换问题
 *
 * @author bigDragon
 * @create 2020-12-31 16:12
 */
public class BeforeJDK8 {
    public static void main(String[] args) {
        BeforeJDK8 beforeJDK8 = new BeforeJDK8();
    }
    @Test
    public void test1(){
        //System类中currentTimeMillis():返回当前时间与1970年1月1日0时0分0秒之间以毫秒为单位的时间差
        //称为时间戳
        long time = System.currentTimeMillis();
        System.out.println(time);
    }
    //java.util.Date类
    @Test
    public void test(){
        //构造器一：Date()：创建一个对应当前时间的Date对象
        Date date = new Date();
        System.out.println(date.toString());//Fri Feb 26 16:18:34 CST 2021
        System.out.println(date.getTime());//1614328215194
        //构造器二：创建指定毫秒数的Date对象
        Date date1 = new Date(1614328215194L);
        System.out.println(date1);//Fri Feb 26 16:30:15 CST 2021

    }
    //java.sql.Date
    @Test
    public void test2(){
        //构造器
        java.sql.Date date = new java.sql.Date(1614328215194L);
        System.out.println(date);
        //java.util.Date-->java.sql.Date的转换问题
/*        Date date = new Date();
        java.sql.Date date2=new java.sql.Date(date.getTime());*/
    }
    //SimpleDateFormat
    @Test
    public void test3() throws ParseException {
        //实例化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        System.out.println(simpleDateFormat.format(new Date()));//21-3-2 下午4:30

        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simpleDateFormat2.format(new Date()));//2021-03-02 16:36:13

        //格式化
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simpleDateFormat3.format(new Date()));//2021-03-02 16:36:13

        //解析
        SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simpleDateFormat4.parse("2020-11-10 23:01:01"));//Tue Nov 10 23:01:01 CST 2020
    }
    //Calendar
    @Test
    public void test4(){
        //实例化
        Calendar calendar = Calendar.getInstance();
        //常用方法
        //get
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        System.out.println(dayOfMonth);//当月的第几天：2
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        System.out.println(dayOfYear);//当年的第几天：61
        //set
        calendar.set(Calendar.DAY_OF_MONTH,22);
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH));//22
        System.out.println(calendar.get(Calendar.DAY_OF_YEAR));//81
        //add
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.MONTH,1);
        System.out.println(calendar2.get(Calendar.DAY_OF_YEAR));
        //减
        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.DAY_OF_YEAR,-1);
        System.out.println(calendar3.get(Calendar.DAY_OF_YEAR));//61

        //getTime
        Calendar calendar4 = Calendar.getInstance();
        Date time = calendar4.getTime();
        System.out.println(time);//Wed Mar 03 16:11:24 CST 2021
        //setTime
        Calendar calendar5 = Calendar.getInstance();
        Date date = new Date();
        calendar5.setTime(date);
        System.out.println(calendar5.get(Calendar.DAY_OF_MONTH));//3

        //特殊注意点
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));//2021-03-03
        Calendar calendar6 = Calendar.getInstance();
        System.out.println(calendar6.get(Calendar.MONTH));//2
        System.out.println(calendar6.get(Calendar.DAY_OF_WEEK));//4
    }


}
