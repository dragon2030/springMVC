package com.bigDragon.javase.baseTest;


/**
 * 接口的使用实例
 *
 * 1.提现了接口的多态性
 * 2.USB中接口的概念与java中接口概念类似，都提供了一种规范，驱动就好比实现类，实现了USB的具体实现
 *
 * @author bigDragon
 * @create 2021-04-14 11:32
 */
public class USB_test {
    public static void main(String[] args) {
        Computer computer = new Computer();
        //创建了接口的非匿名实现类的非匿名对象
        Flash flash = new Flash();
        computer.transferData(flash);
        //创建了接口的非匿名实现类的匿名对象
        computer.transferData(new Flash());
        //创建了接口的匿名实现类的非匿名对象
        USB phone = new USB() {
            @Override
            public void start() {
                System.out.println("手机开始工作");
            }

            @Override
            public void stop() {
                System.out.println("手机结束工作");
            }
        };
        computer.transferData(phone);
        //创建了接口的匿名实现类的匿名对象
        computer.transferData(new USB() {
            @Override
            public void start() {
                System.out.println("mp3开始工作");
            }

            @Override
            public void stop() {
                System.out.println("mp3结束工作");
            }
        });
    }
}
class Computer{
    public void transferData(USB usb){
        usb.start();
        System.out.println("具体传输数据的细节");
        usb.stop();
    }
}
interface USB{//定义了一种规范
    //常量：定义了长、宽、最大最小传输速度等

    void start();

    void stop();
}
class Flash implements USB{

    @Override
    public void start() {
        System.out.println("U盘开始工作");
    }

    @Override
    public void stop() {
        System.out.println("U盘结束工作");
    }
}