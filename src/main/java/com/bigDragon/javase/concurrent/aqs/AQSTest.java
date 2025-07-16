package com.bigDragon.javase.concurrent.aqs;

import java.util.concurrent.Semaphore;

public class AQSTest {
    public void SemaphoreTest() throws InterruptedException {
        Semaphore semaphore = new Semaphore(3); // 允许最多3个线程同时访问
    
        semaphore.acquire(); // 获取1个许可证（如果无可用许可证，线程阻塞）
        try {
            // 临界区代码（受保护的资源访问）
        } finally {
            semaphore.release(); // 释放许可证
        }
    }
}
