package com.bigDragon;

/**
 * @author bigDragon
 * @create 2020-12-01 20:34
 */
public class Clerk {
    private Customer customer;
    private int count = 20;

    public Clerk() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

class productor implements Runnable {
    private final Clerk clerk;
    private int count;

    public productor(Clerk clerk) {
        this.clerk = clerk;
    }

    public Clerk getClerk() {
        return clerk;
    }


    @Override
    public void run() {
        while (true) {
            //System.out.println(2);
            synchronized (Clerk.class) {
            if (clerk.getCount() < 20) {

                    clerk.setCount(clerk.getCount() + 1);
                    System.out.println("生产了一个,当前总数:" + clerk.getCount());
                }
            }
        }
    }
}

class Customer implements Runnable {
    private int count = 0;
    private final Clerk clerk;

    public Customer(Clerk clerk) {
        this.clerk = clerk;
    }

    public Clerk getClerk() {
        return clerk;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public void run() {
        while (true) {
           // System.out.println(1);
            synchronized (Clerk.class) {
            if (clerk.getCount() > 0) {
                    clerk.setCount(clerk.getCount() -1);
                    System.out.println("买了一个，当前总数:"+clerk.getCount());
                }
            }
        }
    }
}

class SaleAndBuy {
    public static void main(String[] args) {
        Clerk clerk = new Clerk();
        Customer customer = new Customer(clerk);
        productor productor = new productor(clerk);
        Thread thread = new Thread(customer);
        thread.setName("消费者");
        Thread thread1 = new Thread(productor);
        thread1.setName("生产者");
        thread1.start();
        thread.start();
    }
}
