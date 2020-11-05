package com.bigDragon.javase.enumTest;

/**
 * enum关键字定义枚举类
 * @author bigDragon
 * @create 2020-10-26 14:42
 */
enum  Season2 {
    //1.提供当前枚举类的对象，多个对象之间用“，”隔开，末尾对象“;”结束
    SPRING("春天","春暖花开"),
    SUMMER("夏天","夏日炎炎"),
    AUTUMN("秋天","秋高气爽"),
    WINTER("冬天","冰天雪地");

    //2、声明Session对象的属性：private final修饰
    private final String seasonName;
    private final String seasonDesc;

    //2.私有化类的构造器，并给对象属性赋值
    private Season2(String seasonName, String seasonDesc){
        this.seasonName = seasonName;
        this.seasonDesc = seasonDesc;
    }

    //其他诉求1：获取枚举类对象的属性
    public String getSeasonName() {
        return seasonName;
    }

    public String getSeasonDesc() {
        return seasonDesc;
    }

}
