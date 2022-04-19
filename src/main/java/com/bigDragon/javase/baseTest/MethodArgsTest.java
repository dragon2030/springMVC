package com.bigDragon.javase.baseTest;

import java.util.Arrays;

/**
 * @author bigDragon
 * @create 2021-03-29 19:33
 */
public class MethodArgsTest {
    public static void main(String[] args) {
        MethodArgsTest methodArgsTest = new MethodArgsTest();
        methodArgsTest.show();//[]
        methodArgsTest.show("hello");//[hello]
        methodArgsTest.show("hello","world");//[hello, world]
        methodArgsTest.show2(new String[]{"hello","world"});
    }
    public void show(String ... strings){
        System.out.println(Arrays.toString(strings));
    }
    public void show2(String[] strings){}
}
