package com.bigDragon.javase.baseTest.FactoryTest;

import com.bigDragon.javase.baseTest.FactoryTest.FactoryFunction.FactoryFunction;
import com.bigDragon.javase.baseTest.FactoryTest.NoFactoryTest.NoFactoryTest;
import com.bigDragon.javase.baseTest.FactoryTest.SimpleFactory.SimpleFactory;

/**
 * 工厂设计模式
 * @author bigDragon
 * @create 2021-04-14 14:59
 */
public class FactoryTestMain {
    public static void main(String[] args) {
        //无工厂模式
        new NoFactoryTest();
        //简单工厂模式
        new SimpleFactory();
        //工厂方法模式
        new FactoryFunction();
        //抽象工厂模式
    }
}
