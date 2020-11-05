package com.bigDragon.javase.enumTest;

import com.bigDragon.javase.Annotation.MyAnnotation;

/**
 * 枚举类
 *
 * 一、枚举类的使用
 *     1.枚举类的理解：类的对象只有有限个，确定的。我们称此类为枚举类。
 *     2.当需要定义一组常量时，强烈建议使用枚举类。
 *     3.如果枚举类中只有一个对象，则可以作为单例模式的实现方式
 *
 * 二、如何定义枚举类
 *      方式一：jdk5.0之前，自定义枚举类
 *      方式二：jdk5.0，可以使用enum关键字定义枚举类
 *
 * 三、Enum类中的常用方法：
 *      values()方法：返回枚举类型的对象数组，该方法可以很方便地遍历所有的枚举值。
 *      valueOf(String str)：可以把一个字符串转换为对应的枚举类对象。要求字符串必须是枚举类对象名称
 *      toString():返回当前枚举类对象常量的名称
 *
 * 四、使用enum关键字定义的枚举类实现接口的情况
 *      情况一：实现接口，在enum类中实现抽象方法
 *      情况二：让枚举类的对象分别实现接口中的抽样方法
 * @author bigDragon
 * @create 2020-10-26 11:02
 */
public class EnumMain {

    public static void main(String[] args){
        EnumMain enumMain = new EnumMain();

        //自定义枚举类
        Season season = Season.WINTER;
        System.out.println(season.getSeasonName()+"\n"+season.getSeasonDesc()+"\n"+season.toString());

        //enum关键字定义枚举类
        Season2 season2 = Season2.WINTER;
        System.out.println(season2.getSeasonName()+"\n"+season2.getSeasonDesc()+"\n"+season2.toString());
        System.out.println(Season2.class.getSuperclass()+" "+season2.getClass().getSuperclass());//父类为java.lang.Enum

        //enum常用方法
        //toString():返回属性的名称
        System.out.println(Season2.WINTER.toString());
        //values():获取枚举类所有属性
        Season2[] values = Season2.values();
        for(int i = 0;i<values.length;i++){
            System.out.println(values[i]);
        }
        //valueOf(String objName):根据提供的objName，返回枚举类中对象名是objName的对象
        Season2 winter = Season2.valueOf("WINTER");
        System.out.println(winter);

        //使用enum关键字定义的枚举类实现接口的情况
        //情况一：实现接口，在enum类中实现抽象方法
        Season3 season3 = Season3.valueOf("WINTER");
        season3.show();
        //情况二：让枚举类的对象分别实现接口中的抽样方法
        Season4[] season4s = Season4.values();
        for(int i = 0;i<season4s.length;i++){
            System.out.println(season4s[i]);
            season4s[i].show();
        }
    }
}
