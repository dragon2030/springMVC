package com.bigDragon.testMain;

/**
 * @author: bigDragon
 * @create: 2022/4/8
 * @Description:
 */
public class Student {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                '}';
    }
}
