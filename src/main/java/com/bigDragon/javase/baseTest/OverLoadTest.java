package com.bigDragon.javase.baseTest;

/**
 * @author bigDragon
 * @create 2021-03-29 14:40
 */
public class OverLoadTest {
    public void overLoad(int i,String st){
        System.out.println(1);
    }

    public void overLoad(String st,int i){
        System.out.println(2);
    }

    public static void main(String[] args) {
        new OverLoadTest().overLoad(1,"1");//1
        new OverLoadTest().overLoad("1",1);//2
    }
}
