package com.bigDragon.javase.DateAPI;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * jdk 8之后日期和时间的API测试

 * @author bigDragon
 * @create 2020-12-31 16:12
 */
public class AfterJDK8 {
    @Test
    public void test1(){
        //可变性
        Calendar.getInstance().set(Calendar.DAY_OF_MONTH,1);
        //偏移性
        System.out.println(new Date(2021-1900,3-1,3));//Wed Mar 03 00:00:00 CST 2021
        //格式化
        System.out.println(new SimpleDateFormat().format(new Date()));//21-3-3 下午7:19
    }
    //Instant

    @Test
    public  void test2(){
        //now():实例化方式，获取本初子午线对应的标准时间
        Instant now = Instant.now();
        System.out.println(now);//2021-03-03T11:52:49.228Z

        Instant now2 = Instant.now().plusMillis(TimeUnit.HOURS.toMillis(8));
        System.out.println(now2);//2021-03-04T17:11:28.498Z

        //atOffset():添加时间偏移量获得当前时区时间
        OffsetDateTime offsetDateTime = Instant.now().atOffset(ZoneOffset.ofHours(8));
        System.out.println(offsetDateTime);//2021-03-03T19:54:22.452+08:00

        //toEpochMilli()：获取自1970年1月1日0时0分0秒（UTC）开始的毫秒数（时间戳） -->Date().getTime()
        long milli = Instant.now().toEpochMilli();
        System.out.println(milli);

        //ofEpochMilli():通过给定的毫秒数（时间戳），获取Instant实例 -->new Date(long date)
        Instant instant = Instant.ofEpochMilli(new Date().getTime());
        System.out.println(instant);//2021-03-03T11:58:44.302Z
    }
    /*
    DateTimeFormatter:格式化或解析日期时间
    类似于SimpleDateFormat
     */
    @Test
    public void test3(){
        //实例化
        //方式一：预定义的标准格式
        // ISO_LOCAL_DATE_TIME
/*        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        //格式化：日期-->字符串
        LocalDateTime now = LocalDateTime.now();
        String format = dateTimeFormatter.format(now);
        System.out.println(now);//2021-03-04T13:35:14.847
        System.out.println(format);//2021-03-04T13:35:14.847
        //解析：字符串-->日期
        TemporalAccessor parse = dateTimeFormatter.parse("2021-03-04T11:06:00.812");
        System.out.println(parse);//{},ISO resolved to 2021-03-04T11:06:00.812

        // ISO_LOCAL_DATE
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ISO_LOCAL_DATE;
        //格式化：日期-->字符串
        LocalDateTime now = LocalDateTime.now();
        String format = dateTimeFormatter.format(now);
        System.out.println(now);//2021-03-04T14:45:30.480
        System.out.println(format);//2021-03-04
        //解析：字符串-->日期
        TemporalAccessor parse = dateTimeFormatter.parse("2021-03-04");
        System.out.println(parse);//{},ISO resolved to 2021-03-04

        // ISO_LOCAL_TIME
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ISO_LOCAL_TIME;
        //格式化：日期-->字符串
        LocalDateTime now = LocalDateTime.now();
        String format = dateTimeFormatter.format(now);
        System.out.println(now);//2021-03-04T14:45:30.480
        System.out.println(format);//14:46:40.174
        //解析：字符串-->日期
        TemporalAccessor parse = dateTimeFormatter.parse("14:46:40.174");
        System.out.println(parse);//{},ISO resolved to 14:46:40.174

        //方式二：本地化相关的格式
        //ofLocalizedDateTime
        //FormatStyle.LONG/FormatStyle.MEDIUM/FormatStyle.SHORT:适用于ofLocalizedDateTime
        DateTimeFormatter dateTimeFormatter3 = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
        System.out.println(dateTimeFormatter3.format(LocalDateTime.now()));//2021年3月4日 下午03时04分24秒
        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        System.out.println(dateTimeFormatter2.format(LocalDateTime.now()));//2021-3-4 15:04:24
        DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        System.out.println(dateTimeFormatter1.format(LocalDateTime.now()));//21-3-4 下午3:03

        //ofLocalizedDate
        //FormatStyle.FULL/FormatStyle.LONG/FormatStyle.MEDIUM/FormatStyle.SHORT:适用于ofLocalizedDate
        DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
        System.out.println(dateTimeFormatter1.format(LocalDateTime.now()));//2021年3月4日 星期四
        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
        System.out.println(dateTimeFormatter2.format(LocalDateTime.now()));//2021年3月4日
        DateTimeFormatter dateTimeFormatter3 = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
        System.out.println(dateTimeFormatter3.format(LocalDateTime.now()));//2021-3-4
        DateTimeFormatter dateTimeFormatter4 = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
        System.out.println(dateTimeFormatter4.format(LocalDateTime.now()));//21-3-4
    */
        //方式三：自定义的格式 （用的最常用）
        //ofPattern("yyyy-MM-dd HH:mm:ss")
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //System.out.println(dateTimeFormatter.format(Instant.now()));
        //格式化
        //System.out.println(dateTimeFormatter.format(LocalDateTime.now()));//2021-03-04 15:52:14
        //解析
       // System.out.println(dateTimeFormatter.parse("2021-03-04 15:52:14"));//{},ISO resolved to 2021-03-04T15:52:14

    }
    //LocalDate:　LocalDate是日期处理类
    @Test
    public void test4(){
        // 获取当前日期
        LocalDate now = LocalDate.now();
        System.out.println(now);//2021-03-04
        // 设置日期
        LocalDate localDate = LocalDate.of(2019, 9, 10);
        System.out.println(localDate);//2019-09-10
        // 获取年
        int year = localDate.getYear();
        int year1 = localDate.get(ChronoField.YEAR);
        System.out.println(year+"  "+year1);//2019  2019
        // 获取月
        Month month = localDate.getMonth();
        int month1 = localDate.get(ChronoField.MONTH_OF_YEAR);
        System.out.println(month+" "+month1);//SEPTEMBER 9
        // 获取日
        int day = localDate.getDayOfMonth();
        int day1 = localDate.get(ChronoField.DAY_OF_MONTH);
        System.out.println(day+"  "+day1);//10  10
        // 获取星期
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        int dayOfWeek1 = localDate.get(ChronoField.DAY_OF_WEEK);
        System.out.println(dayOfWeek+"  "+dayOfWeek1);//TUESDAY  2
    }
    //LocalTime:　LocalTime是时间处理类
    @Test
    public void test5(){
        // 获取当前时间
        LocalTime now = LocalTime.now();
        System.out.println(now);//16:43:21.985
        // 设置时间
        LocalTime localTime = LocalTime.of(13, 51, 10);
        System.out.println(localTime);//13:51:10
        //获取小时
        int hour = localTime.getHour();
        int hour1 = localTime.get(ChronoField.HOUR_OF_DAY);
        System.out.println(hour+"  "+hour1);//13  13
        //获取分
        int minute = localTime.getMinute();
        int minute1 = localTime.get(ChronoField.MINUTE_OF_HOUR);
        System.out.println(minute+"  "+minute1);//51  51
        //获取秒
        int second = localTime.getSecond();
        int second1 = localTime.get(ChronoField.SECOND_OF_MINUTE);
        System.out.println(second+"  "+second1);//10  10
    }
    //LocalDateTime(常用)
    //LocalDateTime可以设置年月日时分秒，相当于LocalDate + LocalTime
    @Test
    public void test6(){
/*        // 获取当前日期时间:LocalDateTime.now()
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);//2021-03-04T16:53:30.329
        // 设置日期LocalDateTime.of()
        LocalDateTime localDateTime1 = LocalDateTime.of(2019, Month.SEPTEMBER, 10, 14, 46, 56);
        System.out.println(localDateTime1);//2019-09-10T14:46:56
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        LocalDateTime localDateTime2 = LocalDateTime.of(localDate, localTime);
        System.out.println(localDateTime2);//2021-03-04T16:53:30.330
        //atTime():Combines this date with a time to create a {@code LocalDateTime}
        LocalDateTime localDateTime3 = localDate.atTime(localTime);
        System.out.println(localDateTime3);//2021-03-04T16:53:30.330
        //atDate():Combines this time with a date to create a {@code LocalDateTime}.
        LocalDateTime localDateTime4 = localTime.atDate(localDate);
        System.out.println(localDateTime4);//2021-03-04T16:53:30.330*/
        //增加一年
        LocalDateTime localDateTime = LocalDateTime.now().plusYears(1);
        LocalDateTime localDateTime2 =  LocalDateTime.now().plus(1, ChronoUnit.YEARS);
        System.out.println(localDateTime+"  "+localDateTime2);//2022-03-04T17:22:10.780  2022-03-04T17:22:10.780
        //减少一个月
        LocalDateTime localDateTime3 = LocalDateTime.now().minusMonths(1);
        LocalDateTime localDateTime4 = LocalDateTime.now().minus(1, ChronoUnit.MONTHS);
        System.out.println(localDateTime3+"  "+localDateTime4);//2021-02-04T17:22:10.781  2021-02-04T17:22:10.781
        //修改年为2019
        LocalDateTime localDateTime5 = LocalDateTime.now().withYear(2020);
        System.out.println(localDateTime5);//2020-03-04T19:09:13.562
        //修改为2022
        LocalDateTime localDateTime6 = LocalDateTime.now().with(ChronoField.YEAR, 2022);
        System.out.println(localDateTime6);//2022-03-04T19:09:13.562
        // 获取LocalDate
        LocalDate localDate2 = localDateTime.toLocalDate();
        System.out.println(localDate2);//2021-03-04
        // 获取LocalTime
        LocalTime localTime2 = localDateTime.toLocalTime();
        System.out.println(localTime2);//16:53:30.329
    }
    //查询相隔时间的计算
    @Test
    public void test7(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");

        LocalDate localDate = LocalDate.of(2020, 9, 10);
        LocalDate localDate2 = LocalDate.of(2020, 1, 1);
        LocalTime localTime = LocalTime.of(0, 0, 0);
        LocalDateTime localDateTime = localDate.atTime(localTime);
        LocalDateTime localDateTime2 = localDate2.atTime(localTime);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime zonedDateTime2 = localDateTime2.atZone(ZoneId.systemDefault());
        Instant instant = zonedDateTime.toInstant();
        Instant instant2 = zonedDateTime2.toInstant();
        long l = instant.toEpochMilli() - instant2.toEpochMilli();
        System.out.println(l);
        System.out.println(l/1000/60/60/24);
        System.out.println(Date.from(instant));
        System.out.println(Date.from(instant2));
        System.out.println(simpleDateFormat.format(Date.from(instant)));
        System.out.println(simpleDateFormat.format(Date.from(instant2)));

        Calendar calendar = Calendar.getInstance();
        calendar.set(2020,8,10,0,0,0);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2020,0,1,0,0,0);
        long time = calendar.getTime().getTime();
        long time2 = calendar2.getTime().getTime();
        long l1 = time - time2;
        System.out.println(l1);
        System.out.println(l1/1000/60/60/24);
        System.out.println(calendar.getTime());
        System.out.println(calendar2.getTime());
        System.out.println(simpleDateFormat.format(calendar.getTime()));
        System.out.println(simpleDateFormat.format(calendar2.getTime()));
        System.out.println(calendar.get(Calendar.SECOND));
    }

}


