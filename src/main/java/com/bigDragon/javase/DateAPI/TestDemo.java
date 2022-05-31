package com.bigDragon.javase.DateAPI;

import cn.hutool.core.date.DateUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;


/**
 * @author: bigDragon
 * @create: 2021/12/28
 * @Description:
 */
public class TestDemo {
    //2021-12-28 业务场景 要求获取前3天的申请记录，T T-1 T-2 jdk1.8前
    @Test
    public void dateOffset() throws ParseException {
        Calendar instance = Calendar.getInstance();
        System.out.println(instance.getTime());
        instance.add(Calendar.DAY_OF_MONTH,-2);
        System.out.println(instance.getTime());

/*        instance.set(Calendar.HOUR_OF_DAY,0);
        instance.set(Calendar.MINUTE,0);
        instance.set(Calendar.SECOND,0);
        System.out.println(instance.getTime());
        System.out.println(instance.getTime().getTime());
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-26"));
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-26").getTime());*/
        //毫秒数改变不了 不可行
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(instance.getTime());
        Date parse = simpleDateFormat.parse(format);
        System.out.println(parse);
        System.out.println(parse.getTime());
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-26"));
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-26").getTime());
    }
    //2021-12-28 业务场景 要求获取前3天的申请记录，T T-1 T-2 jdk1.8后
    @Test
    public void dateOffset2() throws ParseException {
        System.out.println(LocalDateTime.now());
        LocalDateTime localDateTime4 = LocalDateTime.now().minus(2, ChronoUnit.DAYS);
        System.out.println(localDateTime4);
        LocalDate localDate = localDateTime4.toLocalDate();
        LocalTime localTime = LocalTime.of(0, 0, 0);
        LocalDateTime localDateTime = localDate.atTime(localTime);
        System.out.println(localDateTime);
        //LocalDateTime转Date
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        Date date = Date.from(zonedDateTime.toInstant());
        System.out.println(date.getTime());
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-26").getTime());
    }

    //2022-04-08 获取1995年1月28日的Date
    public void specificDate() {
        //jdk1.8前
/*        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,1995);
        calendar.set(Calendar.MONTH,01);
        calendar.set(Calendar.DAY_OF_MONTH,28);
        Date time = calendar.getTime();
        System.out.println(time);*/
//        LocalDate localDate = LocalDate.of(1995,1,28);
//        ZoneId zoneId = ZoneId.systemDefault();
//        ZonedDateTime zdt = localDate.atZone(zoneId);
    }

    public static void main(String[] args) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_MONTH,-0);
        System.out.println(instance.getTime());
    }
}
