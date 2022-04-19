package com.bigDragon.javase.baseTest.InnerClassTest;

/**
 * 类的内部成员之五：内部类
 *
 * 关注如下3个问题
 * 1.如何实例化成员内部类的对象
 * 2.如何在成员内部类中区分调用内部类的结构
 * 3.开发中局部内部类的使用
 *
 * @author bigDragon
 * @create 2021-04-15 13:56
 */
public class InnerClassTest {
    public static void main(String[] args) {
        //创建Dog实例（静态的成员内部类）
        Person.Dog dog = new Person.Dog();
        dog.show();
        //创建bird实例（非静态的成员内部类）
        Person.BirdC birdC = new Person().new BirdC();
        birdC.sing();


    }
}
//成员内部类 vs 局部内部类
class Person{
    String Person_name;
    int Person_age;
    //static
   public void eat(){
       System.out.println("人吃饭");
   }

    //静态成员内部类
    static class Dog{
       String name;
       void show(){
           System.out.println("卡拉是条狗");
       }
    }

    //非静态成员内部类
    public class Bird {
       //成员内部类调用成员内部类
        void method(){
            Person person = new Person();
            BirdC birdC = person.new BirdC();
            birdC.sing();
        }

    }
    //作为一个类
    abstract class BirdB{
        public BirdB(){//无参构造器   

        }

        String name;//属性

        public void sing(){//方法
            eat();
        }

        {//代码块

        }
    }
    // 作为外部类的成员
    public class BirdC{
        String name;

        public void sing(){//方法
            System.out.println(Person_name);//在成员内部类中调用外部类的属性
            System.out.println(Person.this.Person_name);
            eat();
            Person.this.eat();//在成员内部类中调用外部类的方法
        }
        //在成员内部类中区分调用内部类的结构
        public void method(String name){
            System.out.println(name);//方法的形参
            System.out.println(this.name);//内部类的属性
            System.out.println(Person.this.Person_name);//外部类的属性
        }
    }

    {//代码块内
        //局部内部类
        class B{

        }
    }

    public Person(){//构造器内
        //局部内部类
        class CC{

        }
    }

    public void method(){//方法内
        //局部内部类
        class AA{

        }
    }
}