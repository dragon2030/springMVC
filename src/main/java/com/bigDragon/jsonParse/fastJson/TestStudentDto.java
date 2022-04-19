package com.bigDragon.jsonParse.fastJson;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * @author: bigDragon
 * @create: 2022/4/8
 * @Description:
 */
@Data
public class TestStudentDto {
    @JSONField(name="ID")
    private int id;

    @JSONField(name="last_name")
    private String lastName;

    @JSONField(format="yyyy-MM-dd")
    public Date birthDate;
}
