package com.bigDragon.javase.baseTest.InnerClassTest;

/**
 * @author bigDragon
 * @create 2021-04-15 20:21
 */
public class InnerClassTest2 {

    //此种开发中很少见
    public void method(){
        class A{

        }
    }

    //返回了一个实现Comparable接口的对象
    public Comparable getComparable(){
        //方式一
/*        class MyComparable implements Comparable{

            @Override
            public int compareTo(Object o) {
                return 0;
            }
        }
        return new MyComparable();*/
        //方式二：匿名实现类的匿名对象
        return new Comparable(){
            @Override
            public int compareTo(Object o) {
                return 0;
            }
        };
    }
}
