package com.bigDragon.javase.assertTest;

import org.junit.Test;

/**
 * java中的断言
 *
 * 断言：也就是所谓的assertion，是jdk1.4后加入的新功能。
 * 它主要使用在代码开发和测试时期，用于对某些关键数据的判断，如果这个关键数据不是你程序所预期的数据，程序就提出警告或退出。
 *
 * 当软件正式发布后，可以取消断言部分的代码。java中使用assert作为断言的一个关键字，这就可以看出java对断言还是很重视的，因为如果不是很重要的话，
 * 直接开发个类就可以了，没必要新定义一个关键字。
 *
 *  语法1：assert expression;                  //expression代表一个布尔类型的表达式，如果为真，就继续正常运行，如果为假，程序退出
 *
 *  语法2：assert expression1 : expression2;   //expression1是一个布尔表达式，expression2是一个基本类型或者Object类型，如果expression1为真，
 *                                            //则程序忽略expression2继续运行；如果expression1为假，则运行expression2，然后退出程序。
 *  原文：https://www.cnblogs.com/hujingwei/p/5147236.html
 * @author bigDragon
 * @create 2020-12-08 14:00
 */
public class AssertMain {
    static int i =3;
    public static void main(String[] args){
        AssertMain assertMain = new AssertMain();
        //语法1：assert expression;
        assertMain.test();
        //语法2：assert expression1 : expression2;
        assertMain.test2();
    }
    @Test
    public void test(){
        assert i==1;
    }
    @Test
    public void test2(){
        switch (i) {
            case 1:
                System.out.println("正常");
                break;
            case 2:
                System.out.println("正常");
                break;
            case 5:
                System.out.println("正常");
                break;
            default:
                assert false:"i的值无效";       //如果i的值不是你想要的，程序就警告退出
        }
        System.out.println("如果断言正常，我就被打印");
    }
}
