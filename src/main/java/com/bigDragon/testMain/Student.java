package com.bigDragon.testMain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author: bigDragon
 * @create: 2022/4/8
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private String id;
    private String name;
    private Integer age;
    private String describe;
    private String IDCard;
    private String phoneNo;
    private Integer sort;
    private Dog dog;
    private List<String> children;
}
