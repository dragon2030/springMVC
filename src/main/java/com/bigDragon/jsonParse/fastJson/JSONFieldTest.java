package com.bigDragon.jsonParse.fastJson;

import com.alibaba.fastjson.JSON;

import java.util.Calendar;
import java.util.Date;

/**
 * @author: bigDragon
 * @create: 2022/4/8
 * @Description:
 *  @JSONField 注解使用
 */
public class JSONFieldTest {
    public static void main(String[] args) {
        TestStudentDto testStudentDto = new TestStudentDto();
        testStudentDto.setId(123);
        testStudentDto.setLastName("Jack");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,1995);
        calendar.set(Calendar.MONTH,01);
        calendar.set(Calendar.DAY_OF_MONTH,28);
        testStudentDto.setBirthDate(calendar.getTime());
        System.out.println(JSON.toJSONString(testStudentDto));
    }
}
