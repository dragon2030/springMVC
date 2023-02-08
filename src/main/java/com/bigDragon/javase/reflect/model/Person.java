package com.bigDragon.javase.reflect.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author bigDragon
 * @create 2020-12-08 16:44
 */
@MyAnnotation(value = "hi")
@Data
@NoArgsConstructor
public class Person extends Creature<String> implements Comparable<String>,MyInterface{
    private String name;
    int age;
    public int id;
    public static int sex = 1;
    private List<Student> studentList;
    private Student student;

    @MyAnnotation(value = "abc")
    private Person(String name) {
        this.name = name;
    }

    @MyAnnotation
    private String show(String nation){
        System.out.println("我的国籍是："+nation);
        return nation;
    }

    public String display(String interests,int age) throws NullPointerException,ClassCastException{
        return interests+age;
    }

    @Override
    public void info() {
        System.out.println("我是一个人");
    }

    @Override
    public int compareTo(String o) {
        return 0;
    }

    private static void showDesc(){
        System.out.println("我是一个可爱的人");
    }


}
