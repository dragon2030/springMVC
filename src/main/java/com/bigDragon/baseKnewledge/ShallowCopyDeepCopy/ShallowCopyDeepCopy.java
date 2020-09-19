package com.bigDragon.baseKnewledge.ShallowCopyDeepCopy;


/**
 * Java 浅拷贝和深拷贝
 *
 * 应用场景：开发过程中，有时会遇到把现有的一个对象的所有成员属性拷贝给另一个对象的需求。
 *
 * Java中的数据类型：
 *      Java中的数据类型分为基本数据类型和引用数据类型。对于这两种数据类型，在进行赋值操作、用作方法参数或返回值时，会有值传递和引用地址传递的差别

 * 对象拷贝
 *      特点：对象拷贝没有生成新的对象，两者的对象地址是一样的。
 * 浅拷贝
 *      浅拷贝介绍：浅拷贝是按位拷贝的对象，它创建一个新的对象，这个对象有着原始对象属性值的一份精准拷贝。如果属性是基本类型，拷贝的就是基本类型的值；如果属性是内存地址（引用类型），拷贝的
 *      就是内存地址，因此如果其中一个对象改变了这个地址，就会影响到另一个对象。即默认拷贝构造函数只是对对象进行浅拷贝复制，即只复制对象空间而不复制资源。
 *      浅拷贝特点：
 *          1.对于基本类型数据类型的成员对象，因为基础数据类型是值传递的，所以是直接将属性值赋值给新的对象。基础类型的拷贝，其中一个对象修改该值，不会影响另一个
 *          2.对于引用类型，比如数组或者类对象，因为引用类型是引用传递，所以浅拷贝只是把内存地址赋值给了成员变量，它们指向了同一个内存地址。要改变其中一个，会对另一个也产生影响
 * 深拷贝
 *      深拷贝介绍：深拷贝，在拷贝引用类型成员变量时，为引用类型的数据成员另辟了一个独立的内存空间，实现真正内容上的拷贝
 *      深拷贝特点：
 *          1.对基本数据类型的成员对象，因为集成数据类型是值传递的，所以是直接将属性赋值给新的对象。鸡翅类型的拷贝，其中一个对象修改该值，不会影响另一个。（和浅拷贝一样）
 *          2.对于引用类型，比如数组或者类对象，深拷贝会新建一个对象空间，然后拷贝里面的内容，所以他们指向来了不同的内存空间。改变其中一个，不会对另一个产生影响。
 *          3.对于多层对象的，每个对象都需要实现Cloneable并重写clone()方法，进而实现了对象的串行层层拷贝。
 * @author bigDragon
 * @create 2020-09-19 9:46
 */
public class ShallowCopyDeepCopy {
    public static void main(String[] args){
        ShallowCopyDeepCopy shallowCopyDeepCopy = new ShallowCopyDeepCopy();
        //对象拷贝
        System.out.println("对象拷贝");
        shallowCopyDeepCopy.ObjectCloneTest();
        //浅拷贝
        System.out.println("浅拷贝");
        shallowCopyDeepCopy.ShallowCopy();
        //深拷贝
        System.out.println("深拷贝");
        shallowCopyDeepCopy.DeepCopy();
    }

    /**
     * 对象拷贝
     */
    public void ObjectCloneTest(){
        //拷贝user1到user2中
        ClonePOJO clonePOJO = new ClonePOJO();
        clonePOJO.setStr1("str1");
        clonePOJO.setInt1(1);
        User user1 = new User();
        user1.setName("Mike");
        user1.setAge("22");
        clonePOJO.setUser(user1);
        ClonePOJO cloneTest2 = clonePOJO;
        //改变user2属性
        cloneTest2.setStr1("已久修改对象");
        cloneTest2.setInt1(0);
        User user2=cloneTest2.getUser();
        user2.setName("name已久修改对象");
        user2.setAge("age已久修改对象");
        cloneTest2.setUser(user2);
        System.out.println("目标对象:"+ clonePOJO);
        System.out.println("拷贝结果对象:"+cloneTest2);
    }

    /**
     * 浅拷贝
     */
    public void  ShallowCopy(){
        //拷贝user1到user2中
        ClonePOJO2 clonePOJO2 = new ClonePOJO2();
        clonePOJO2.setStr1("str1");
        clonePOJO2.setInt1(1);
        User user1 = new User();
        user1.setName("Mike");
        user1.setAge("22");
        clonePOJO2.setUser(user1);
        ClonePOJO2 cloneTest2 = (ClonePOJO2)clonePOJO2.clone();
        //改变user2属性
        cloneTest2.setStr1("已久修改对象");
        cloneTest2.setInt1(0);
        User user2=cloneTest2.getUser();
        user2.setName("name已久修改对象");
        user2.setAge("age已久修改对象");
        cloneTest2.setUser(user2);
        System.out.println("目标对象:"+ clonePOJO2);
        System.out.println("拷贝结果对象:"+cloneTest2);
    }

    /**
     * 深拷贝
     */
    public void DeepCopy(){
        //拷贝user1到user2中
        ClonePojo3 clonePojo3 = new ClonePojo3();
        clonePojo3.setStr1("str1");
        clonePojo3.setInt1(1);
        User2 user2 = new User2();
        user2.setName("Mike");
        user2.setAge("22");
        clonePojo3.setUser2(user2);
        ClonePojo3 cloneTest3 = (ClonePojo3)clonePojo3.clone();
        //改变user2属性
        cloneTest3.setStr1("已久修改对象");
        cloneTest3.setInt1(0);
        User2 user3=cloneTest3.getUser2();
        user3.setName("name已久修改对象");
        user3.setAge("age已久修改对象");
        cloneTest3.setUser2(user3);
        System.out.println("目标对象:"+ clonePojo3);
        System.out.println("拷贝结果对象:"+cloneTest3);
    }
}
