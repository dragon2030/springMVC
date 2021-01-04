package com.bigDragon.javase.reflect.model;

import java.io.Serializable;

/**
 * @author bigDragon
 * @create 2020-12-08 16:44
 */
public class Creature<T> implements Serializable {
    private char gender;
    public double weight;

    private void breath(){
        System.out.println("生物呼吸");
    }

    public void eat(){
        System.out.println("生物吃东西");
    }
}
