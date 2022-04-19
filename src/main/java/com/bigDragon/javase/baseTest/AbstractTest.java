package com.bigDragon.javase.baseTest;

import com.bigDragon.javase.reflect.model.Creature;
import org.junit.Test;

/**
 * abstract关键字的使用
 * 1.abstract：抽象的
 * 2.abstract可以用来修饰的结构：类、方法————抽象类、抽象方法
 * 3.abstract修饰类：抽象类
 *      >此类不能被实例化
 *      >抽象类中一定有构造器，便于子类实例化时使用（涉及：子类对象实例化的全过程）
 *      >开发中，都会提供抽象类的子类，让子类对象实例化，完成相关挫折
 * 4.abstract修饰方法：抽象方法
 *
 * @author bigDragon
 * @create 2021-04-13 10:31
 */
public class AbstractTest {
    public static void main(String[] args) {
        new Abstract_Student();
    }
    //抽象类的匿名子类
    @Test
    public void test1(){
        //创建一个匿名子类的对象
        Abstract_Creature creature = new Abstract_Creature(){
            @Override
            void breath() {
                System.out.println("呼吸");
            }
        };
        creature.breath();
    }
    //抽象类的匿名子类的匿名对象
    @Test
    public void test2(){
        //创建一个匿名子类的匿名对象
        new Abstract_Creature(){
            @Override
            void breath() {
                System.out.println("呼吸");
            }
        }.breath();

    }
}

abstract class Abstract_Creature{
    abstract void breath();
    //抽象方法中代码块可以有
    {

    }
    //抽象方法中内部类可以有
    class A{}
}

//抽象类
//如若子类没有重写了父类中所有的抽象方法，则此子类也是一个抽象类，需要使用abstract修饰
abstract class Abstract_Person extends Abstract_Creature {
    String name;
    int age;

    public Abstract_Person() {
    }

    public Abstract_Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    //抽象方法
    public abstract void eat();

     public void walk(){
         System.out.println("人走路");
     }
}
//若子类重写了父类中所有的抽象方法以后，此子类方可实例化
class Abstract_Student extends Abstract_Person{

    public Abstract_Student() {
        super();
    }

    public Abstract_Student(String name, int age) {
        super(name, age);
    }


    @Override
    public void eat() {
        System.out.println("学生多吃有营养的食物");
    }

    @Override
    void breath() {
        System.out.println("学生需要呼吸新鲜空气");
    }
}
