package com.bigDragon.javase.baseTest;

import org.junit.Test;

/**
 * static关键字的使用
 *
 * 1.static:静态的
 * 2.
 * @author bigDragon
 * @create 2021-04-11 14:22
 */
public class StaticTest {

    //静态属性 vs 非静态属性
    @Test
    public void test1(){
        Chinese chinese = new Chinese();
        chinese.name = "姚明";
        chinese.nation = "CHN";

        Chinese chinese2 = new Chinese();
        chinese2.name = "马龙";
        System.out.println(chinese2.name+chinese2.nation);//马龙CHN
        Chinese.show();
        //Chinese.info();//编译不通过
        chinese.info();
        chinese.show();
    }
}
class Chinese{
    String name;
    int age;
    static String nation;
    public Chinese(){
        System.out.println(nation);
    }
    //info方法非静态，此时输出属性不确定
    public void info(){
        System.out.println("name:"+name+" age:"+age);
    }
    public static void show(){
        System.out.println("我是一个中国人");
    }
}
