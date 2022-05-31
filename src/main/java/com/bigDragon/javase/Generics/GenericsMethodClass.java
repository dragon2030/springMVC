package com.bigDragon.javase.Generics;

/**
 * @author: bigDragon
 * @create: 2022/5/12
 * @Description:
 */
public class GenericsMethodClass implements GenericsMethodInterface{
    @Override
    public <T> T genCode() {
        Object object = new Object();
        return (T) object;
    }
}
