package com.bigDragon.testMain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: bigDragon
 * @create: 2022/4/8
 * @Description:
 */
@Accessors(chain = true)
@Data
public class Student {
    private String name;
    private int sort;
    private Student student;

}
