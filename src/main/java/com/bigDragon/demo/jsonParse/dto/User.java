package com.bigDragon.demo.jsonParse.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author: bigDragon
 * @create: 2022/2/9
 * @Description:
 */
@Data
@ToString
public class User {
    private int age;
    private String name;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;
}
