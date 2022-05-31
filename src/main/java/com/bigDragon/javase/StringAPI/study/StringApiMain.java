package com.bigDragon.javase.StringAPI.study;

import org.junit.Test;

/**
 * 字符串相关的类
 *
 * 一、String类及常用方法
 * 1.1 String的特性
 *      >String类：代表字符串。Java程序中所有字符串字面值（如“abc”）都作为此类的实例实现。
 *      >String是一个final类，代表不可变的字符序列，简称：不可变性
 *      >字符串是常量，用双引号引起来表示，他们的值在创建之后不能更改
 *      >String对象的字符内容是存储在一个字符数组value[]中的
 *
 *  public final class String
 *     implements java.io.Serializable, CompareAPI<String>, CharSequence {
 *      private final char value[];
 *      private int hash;
 *
 * 二、StringBuffer和StringBuilder
 *
 * @author bigDragon
 * @create 2020-12-24 17:10
 */
public class StringApiMain {
/*    public static void main(String[] args) {
        new StringApiMain();
    }*/

    @Test
    public void case_20220329() {
        double d = 3.1415926;
        String result1 = String.format("%.2f", d);
        String result = String.format("%.3f", d);
        System.out.println(result1);
        System.out.println(result);
    }
}
