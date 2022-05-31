package com.bigDragon.javase.CompareAPI;

import com.bigDragon.testMain.Student;
import org.junit.Test;

import java.util.*;

/**
 * @author bigDragon
 * @create 2021-03-04 20:50
 */
public class ComparatorTest {
    public static void main(String[] args) {
        String[] array=new String[]{"aa","cc","bb"};
        Arrays.sort(array, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        System.out.println(Arrays.toString(array));//[aa, bb, cc]
    }

    //对象按照sort排序
    @Test
    public void test1(){
        List<Student> students = new ArrayList<>();

        Student student2 = new Student();
        student2.setName("John");
        student2.setSort(2);
        students.add(student2);

        Student student1 = new Student();
        student1.setName("Mike");
        student1.setSort(1);
        students.add(student1);

        Student student3 = new Student();
        student3.setName("Bob");
        student3.setSort(3);
        students.add(student3);

        System.out.println(students);
        //students.stream().sorted((e1, e2) -> Integer.compare(e1.getSort(), e1.getSort())).forEach(System.out::println);
        Collections.sort(students, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return Integer.compare(o1.getSort(),o2.getSort());
            }
        });
        System.out.println(students);
        Collections.sort(students, (o1, o2) -> Integer.compare(o1.getSort(),o2.getSort()));
    }
}
