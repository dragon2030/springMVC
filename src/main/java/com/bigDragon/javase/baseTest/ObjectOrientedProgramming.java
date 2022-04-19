package com.bigDragon.javase.baseTest;

import org.junit.Test;

/**
 * 面向对象
 * @author bigDragon
 * @create 2021-03-27 14:23
 */
public class ObjectOrientedProgramming {
    public static void main(String[] args) {
        float price = new Phone().price;
        System.out.println(price);//0.0
        PhoneMall phoneMall = new PhoneMall();
        float v = phoneMall.showPrice(new Phone());
        System.out.println(v);//0.0
    }
}
class Phone{
    public float price;
}
class PhoneMall{
    public float showPrice(Phone phone){
        return phone.price;
    }
}
