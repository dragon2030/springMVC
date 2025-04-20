# 源码解析
```
    final void runWorker(Worker w) {
        Thread wt = Thread.currentThread(); // 获取当前线程
        Runnable task = w.firstTask; // 获取Worker对象中的第一个任务
        w.firstTask = null; // 清空Worker对象中的第一个任务，表示任务被取出执行
        w.unlock(); // 允许中断
    
        boolean completedAbruptly = true; // 标志位，用于记录线程是否突然终止
    
        try {
            // 主循环，不断从工作队列中获取任务并执行
            while (task != null || (task = getTask()) != null) {
                w.lock(); // 加锁以确保线程安全
    
                // 检查线程池状态和中断状态，如果线程池已经处于STOP状态或更高，并且当前线程被中断但未处理，则再次中断当前线程
                if ((runStateAtLeast(ctl.get(), STOP) ||
                     (Thread.interrupted() &&
                      runStateAtLeast(ctl.get(), STOP))) &&
                    !wt.isInterrupted())
                    wt.interrupt();
    
                try {
                    beforeExecute(wt, task); // 执行任务前的准备工作
                    Throwable thrown = null;
                    try {
                        task.run(); // 执行任务的主体逻辑
                    } catch (RuntimeException x) {
                        thrown = x; throw x; // 捕获并抛出RuntimeException
                    } catch (Error x) {
                        thrown = x; throw x; // 捕获并抛出Error
                    } catch (Throwable x) {
                        thrown = x; throw new Error(x); // 捕获并抛出Throwable
                    } finally {
                        afterExecute(task, thrown); // 任务执行完成后的清理工作
                    }
                } finally {
                    task = null; // 重置任务，准备获取下一个任务
                    w.completedTasks++; // 记录完成的任务数
                    w.unlock(); // 释放锁
                }
            }
            completedAbruptly = false; // 循环正常结束，标志位设置为false
        } finally {
            processWorkerExit(w, completedAbruptly); // 处理工作线程的退出
        }
}
```
【并没完全看懂 大致看线程池执行流程和核心参数线程池执行的影响】
