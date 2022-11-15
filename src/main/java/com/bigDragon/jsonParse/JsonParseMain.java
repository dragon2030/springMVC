package com.bigDragon.jsonParse;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.bigDragon.jsonParse.dto.Student;
import org.junit.Test;

/**
 * @author: bigDragon
 * @create: 2022/10/27
 * @Description:
 */
public class JsonParseMain {
    @Test
    public void case_20221027() {
        Student student = new Student();
        student.setAge(22);
        student.setName("Mike");
        String response = JSON.toJSONString(student);
        System.out.println(response);
        Student student1 = JSONUtil.toBean(response, new TypeReference<Student>() {
        }, true);
        System.out.println(student1);
    }
}
