package com.bigDragon.javase.reflect;

/**
 * 测试反射的实体类
 * @author bigDragon
 * @create 2020-12-08 14:51
 */
public class Person2 {
    private String name;
    public int age;

    public Person2(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person2{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
