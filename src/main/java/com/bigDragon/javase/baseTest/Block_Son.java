package com.bigDragon.javase.baseTest;

/**
 * 代码块与构造器执行的先后顺序
 *
 * 说明main方法仍是一个静态方法，需先加载Block_Son类，因为Block_Father是Block_Son父类，所有先执行Block_Father的静态代码块，
 * 后执行Block_Son的静态代码块，然后才执行main方法中的语句，当运行到“new Block_Son()”时，先执行父类Block_Father代码块然后执行
 * Block_Father的构造器，再执行子类Block_Son的代码块和构造器
 *
 * 总结：由父及子，静态先行
 *
 * @author bigDragon
 * @create 2021-04-12 19:44
 */
class Block_Father {
    static {
        System.out.println("11111111111111111");
    }
    {
        System.out.println("22222222222222222");
    }
    public Block_Father(){
        System.out.println("33333333333333333");
    }
}
public class Block_Son extends Block_Father{
    static {
        System.out.println("44444444444444444");
    }
    {
        System.out.println("55555555555555555");
    }
    public Block_Son(){
        System.out.println("66666666666666666");
    }

    public static void main(String[] args) {
        System.out.println("77777777777777777");
        System.out.println("*****************");
        new Block_Son();
    }
}
