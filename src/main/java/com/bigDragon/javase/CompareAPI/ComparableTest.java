package com.bigDragon.javase.CompareAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Java比较器具
 * 一、Comparable接口
 * 二、Comparator接口
 *
 * @author bigDragon
 * @create 2021-03-04 20:08
 */
class ComparableTest implements Comparable<ComparableTest>{
    public static void main(String[] args) {
        //ArrayTest.sort()对对象进行自然排序
        String[] array=new String[]{"aa","cc","bb"};
        Arrays.sort(array);//[aa, bb, cc]

        //Collections.sort对对象进行自然排序
        System.out.println(Arrays.toString(array));
        ComparableTest mike = new ComparableTest("Mike");
        ComparableTest mike2 = new ComparableTest("Bob");
        List<ComparableTest> testClasses = new ArrayList<>();
        testClasses.add(mike);
        testClasses.add(mike2);
        Collections.sort(testClasses);
        System.out.println(testClasses);
    }
    public ComparableTest(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ComparableTest{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public int compareTo(ComparableTest testClass) {
        return this.name.compareTo(testClass.name);
    }

}
