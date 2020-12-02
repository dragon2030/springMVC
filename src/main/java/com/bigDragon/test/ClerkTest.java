package com.bigDragon.test;

/**
 * @author bigDragon
 * @create 2020-12-02 9:42
 */
public class ClerkTest {
    public static int goodsNum = 50;

    public static void main(String[] args){
        new Thread(new Customer(),"消费者1").start();
        new Thread(new Customer(),"消费者2").start();

        new Thread(new Producer(),"生产者1").start();
    }
}

class Producer implements Runnable{
    @Override
    public void run() {
        while(true) {
            //System.out.println("生产者执行，当前商品总数"+ClerkTest.goodsNum);
            synchronized (ClerkTest.class){
                if (ClerkTest.goodsNum < 100) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ClerkTest.goodsNum++;
                    System.out.println(Thread.currentThread().getName()+"生产了一件商品，当前商品总数"+ClerkTest.goodsNum);
                }
            }
        }

    }
}
class Customer implements Runnable{
    @Override
    public void run() {
        while(true){
            //System.out.println("消费者执行，当前商品总数"+ClerkTest.goodsNum);
            synchronized (ClerkTest.class){
                if(ClerkTest.goodsNum > 0){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ClerkTest.goodsNum--;
                    System.out.println(Thread.currentThread().getName()+"消费了一件商品，当前商品总数"+ClerkTest.goodsNum);
                }
            }
        }
    }
}

