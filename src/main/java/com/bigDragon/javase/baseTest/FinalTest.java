package com.bigDragon.javase.baseTest;

/**
 * final:最终的
 * @author bigDragon
 * @create 2021-04-12 20:25
 */
public class FinalTest {
    public static void main(String[] args) {

    }
}

final class FinalA{
}
//class B extends FinalA{}//Cannot inherit from final 'com.bigDragon.javase.baseTest.FinalA'

class AA{
    public final void show(){}
}
class BB extends AA{
    //@Override
    //public void show(){}//'show()' cannot override 'show()' in 'com.bigDragon.javase.baseTest.AA'; overridden method is final
}
class CC{
    final int c = 10;
    public void method(){
        //c=20;//Cannot assign a value to final variable 'c'
    }
}
class FinalTest2{
    final int WIDTH = 0;
    final int LEFT;
    final int RIGHT;
    {
        try {
            "".equals("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        LEFT = 1;
    }
    public FinalTest2(){
        RIGHT = 2;
    }
}
class FinalTest3{
    public void show(){
        final int NUM = 10;
        //NUM =20;//编译不通过
    }
    public void show(final int num){
        //num = 20;//编译不通过
    }
}
class FinalTest4{
    public void method(final People p){
        p.age = 20;
    }

    public static void main(String[] args) {
        FinalTest4 finalTest4 = new FinalTest4();
        People people = new People();
        finalTest4.method(people);
        System.out.println(people.age);
    };
}
class People{
    int age = 10;
}
