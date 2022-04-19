package com.bigDragon.timedTask;

/**
 * @author: bigDragon
 * @create: 2022/3/28
 * @Description:
 *      定时任务
 */
public class Main {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        Main main = new Main();
        String name = main.getName();
        System.out.println(name);
        main.setName("Mike");
        System.out.println(name);
        System.out.println(main.getName());
    }
}
