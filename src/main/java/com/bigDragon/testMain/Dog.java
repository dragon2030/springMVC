package com.bigDragon.testMain;

import lombok.Data;

/**
 * @author: bigDragon
 * @create: 2023/7/5
 * @Description:
 */
@Data
public class Dog implements Animal {
    private String name;
    private Integer age;

    @Override
    public void eat() {

    }
}
