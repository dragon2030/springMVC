package com.bigDragon.javase.baseTest;

/**
 * @author bigDragon
 * @create 2021-03-31 19:18
 */
public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.method1(1);//method2 原因:method1不确定个数，method2确定个数，确定个数的有限调用
new String();
    }

    public void method1(int ... i){
        System.out.println("method1");
    }
    public void method1(int i){
        System.out.println("method2");
    }


}

