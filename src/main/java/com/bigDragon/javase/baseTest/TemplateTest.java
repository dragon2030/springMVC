package com.bigDragon.javase.baseTest;

/**
 * 抽象类的应用：模板方法的设计模式
 * @author bigDragon
 * @create 2021-04-13 18:29
 */
public class TemplateTest {
    public static void main(String[] args) {
        Template template = new SubTemplate();
        template.spendTime();
    }
}
abstract class Template{
    //计算某段代码执行所需要的时间
    public void spendTime(){
        long start = System.currentTimeMillis();
        code();//不确定部分
        long end = System.currentTimeMillis();
        System.out.println("花费时间"+(end-start));
    }
    public abstract void code();
}
class SubTemplate extends Template{
    @Override
    public void code() {
        //执行的具体代码
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("code方法执行");
    }
}
