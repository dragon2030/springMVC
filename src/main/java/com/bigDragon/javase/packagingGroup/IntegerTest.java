package com.bigDragon.javase.packagingGroup;

import java.util.Objects;

/**
 * @author: bigDragon
 * @create: 2022/5/29
 * @Description:
 */
public class IntegerTest {
        //  判断Integer类型的值为null或为空
        public static void main(String[] args) {
        Integer i = null;
        Boolean b = i==1;
        System.out.println(b);
        System.out.println(Integer.valueOf(1).equals(i));
        System.out.println(i instanceof Integer);
        System.out.println(Objects.equals(1,null));
        System.out.println(Objects.equals(1,new Integer(1)));

    }
}
