package com.bigDragon.javase.baseTest;

/**
 * @author bigDragon
 * @create 2021-03-31 16:46
 */
public class ThisTest {

    public ThisTest(){
        System.out.println("ThisTest()...");
    }
    public ThisTest(String st1){
        this();
        System.out.println("ThisTest(String st1)...");
        name = st1;
    }
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
