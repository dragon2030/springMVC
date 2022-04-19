package com.bigDragon.javase.baseTest.Polymorphism;

import org.junit.Test;

/**
 * @author bigDragon
 * @create 2021-04-02 20:12
 */
public class Main {
    public static void main(String[] args) {
        Son son = new Son();
        System.out.println(son.id);
        Father f = new Son();
        f.eat();//Son类的eat方法
        System.out.println(f.id);//1 说明：值为父类的属性
        //说明：不能调用子类所持有的方法、属性，编译时f为Father类型
        //f.sonMethod;//编译错误
        //System.out.println(f.sonId);//编译错误
        //向下转型，使用强制类型装换
        Son s =(Son)f;
        System.out.println(s.sonId);//1
        System.out.println(s.fatherId);//1003
        s.sonMethod();
        //instanceof
        System.out.println(f instanceof Son);//true
        System.out.println(f instanceof Father);//true
        if(f instanceof Son){
            Son s1 =(Son)f;
            System.out.println(s.sonId);//1
            System.out.println(s.fatherId);//1003
        }
        System.out.println(f.fatherId);//1001 说明：只能调用父类的属性，子类单独属性调用不了
    }

    //instanceof
    @Test
    public void test1(Father f){
        if(f instanceof Son){
            System.out.println("Son的实例对象");
        }else if(f instanceof Father){
            System.out.println("Father的实例对象");
        }else {
            System.out.println("Object的实例对象");
        }
    }
}
