package com.bigDragon.javase.collection;


/**
 * collection的contains测试的实体类,重写toString()方法
 * @author bigDragon
 * @create 2020-11-11 16:22
 */
public class Person2 {
    private String name;
    private int age;

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

    @Override
    public String toString() {
        return "Person2{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public Person2(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Person2() {
    }
}
