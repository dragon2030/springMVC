package com.bigDragon.javase.reflect.jdkProxy;


/**
 * 被代理类
 * @author bigDragon
 * @create 2021-01-19 17:02
 */
class SuperMan implements Human {

    @Override
    public String getBelief() {
        return "I believe i can fly!";
    }

    @Override
    public void eat(String food) {
        System.out.println("我喜欢吃"+food);
    }
}