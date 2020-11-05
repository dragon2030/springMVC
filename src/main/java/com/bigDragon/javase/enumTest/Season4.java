package com.bigDragon.javase.enumTest;

/**
 * 使用enum关键字定义的枚举类实现接口的情况
 * 情况二：让枚举类的对象分别实现接口中的抽样方法f
 * @author bigDragon
 * @create 2020-10-27 10:55
 */
enum  Season4 implements Info{
    SPRING("春天","春暖花开"){
        @Override
        public void show() {
            System.out.println("在enum类中实现抽象方法SPRING");
        }
    },
    SUMMER("夏天","夏日炎炎"){
        @Override
        public void show() {
            System.out.println("在enum类中实现抽象方法SUMMER");
        }
    },
    AUTUMN("秋天","秋高气爽"){
        @Override
        public void show() {
            System.out.println("在enum类中实现抽象方法AUTUMN");
        }
    },
    WINTER("冬天","冰天雪地"){
        @Override
        public void show() {
            System.out.println("在enum类中实现抽象方法WINTER");
        }
    };

    private final String seasonName;
    private final String seasonDesc;

    private Season4(String seasonName, String seasonDesc){
        this.seasonName = seasonName;
        this.seasonDesc = seasonDesc;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public String getSeasonDesc() {
        return seasonDesc;
    }
}
