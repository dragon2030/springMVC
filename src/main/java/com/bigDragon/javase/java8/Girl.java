package com.bigDragon.javase.java8;

/**
 * Optional测试类
 * @author bigDragon
 * @create 2020-12-21 19:44
 */
public class Girl {
    private String name;

    @Override
    public String toString() {
        return "Girl{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Girl() {
    }

    public Girl(String name) {
        this.name = name;
    }
}
