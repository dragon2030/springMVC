package com.bigDragon.testMain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bigDragon
 * @create 2020-12-09 20:31
 */
public class Main {

    public static void main(String[] args) {
//        Student student = new Student();
//        student.setStudent(student);
//        student.setName("Mike");
//        student.setSort(22);
//        System.out.println(student);
        List<InfoDto> infoDtos = new ArrayList<>();
        int i = 0;
        while (true){
            i++;
            infoDtos.add(new InfoDto(i));
            System.out.println(i);
        }

    }

}
