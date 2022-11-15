package com.bigDragon.testMain;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author bigDragon
 * @create 2020-12-09 20:31
 */
public class Main {

    public static void main(String[] args) {
        Student student = new Student();
        System.out.println(student.getSort()+1);
    }


    public List<Student> test() {
        List<Student> students = new ArrayList<>();
        students.add(new Student("Mike",1));
        students.add(new Student("Sam",2));
        students.add(new Student("Tom",3));
        return students;
    }

    public Map<String, Student> test2() {
        Map<String, Student> stringStudentHashMap = new HashMap<>();
        stringStudentHashMap.put("1",new Student("Mike",1));
        stringStudentHashMap.put("2",new Student("Sam",2));
        stringStudentHashMap.put("3",new Student("Tom",3));
        return stringStudentHashMap;
    }

}
