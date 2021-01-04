package com.bigDragon.javase.DateAPI;

/**
 * 日期时间API
 *
 * 一、JDK8之前的日期时间API
 *      >System静态方法
 *      >Date类
 *      >Calendar类
 *      >SimpleDateFormat类
 * 二、JDK8中新日期时间API
 *      >LocalDate、LocalTime、LocalDateTime
 *      >Instant
 *      >DateTimeFormatter
 *      >其他类
 *
 * @author bigDragon
 * @create 2020-12-24 17:11
 */
public class DateApiMain {
    public static void main(String[] args) {
        //jdk 8之前日期和时间的API测试
        new BeforeJDK8();
        //jdk 8之后日期和时间的API测试
        new AfterJDK8();
    }
}
