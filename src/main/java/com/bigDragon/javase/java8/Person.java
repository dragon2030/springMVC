package com.bigDragon.javase.java8;

import java.util.Objects;

/**
 * lambda表达式测试实体类
 * @author bigDragon
 * @create 2020-12-01 15:49
 */
public class Person {
    private String name;
    public int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
        System.out.println("Person(String name, int age).....");
    }

    public Person(String name) {
        this.name = name;
        System.out.println("Person(String name).....");
    }

    public Person() {
        System.out.println("Person().......");
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

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public void show(){
        System.out.println("你好 我是一个人");
    }

    private String showNation(String nation){
        System.out.println("我的国籍是："+nation);
        return nation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age &&
                Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
