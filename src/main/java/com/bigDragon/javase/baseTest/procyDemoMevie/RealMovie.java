package com.bigDragon.javase.baseTest.procyDemoMevie;

/**
 * 被代理角色(目标类)
 * 真正的实现这个 Movie 接口的类
 *
 * @author bigDragon
 * @create 2021-04-15 9:42
 */
public class RealMovie implements Movie {
    @Override
    public void play() {
        // TODO Auto-generated method stub
        System.out.println("您正在观看电影 《肖申克的救赎》");
    }
}
