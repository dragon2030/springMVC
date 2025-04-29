ReentrantLock 常用方法详解
# 1. 基本锁操作
|方法|说明|
|---|---|
|void lock()|获取锁，如果锁不可用则阻塞|
|void unlock()|释放锁|
|boolean tryLock()|非阻塞尝试获取锁，成功返回true|
|boolean tryLock(long timeout,TimeUnit unit)|带超时的尝试获取锁|
# 2. 锁状态查询
   方法	说明
   boolean isLocked()	查询锁是否被任何线程持有
   boolean isHeldByCurrentThread()	查询锁是否被当前线程持有
   int getHoldCount()	返回当前线程持有锁的次数（可重入计数）
# 3. 队列相关方法
   方法	说明
   boolean hasQueuedThreads()	查询是否有线程等待获取锁
   int getQueueLength()	返回等待获取锁的线程估计数
   boolean hasQueuedThread(Thread thread)	查询指定线程是否在等待队列中
#  4. 公平性控制
   方法	说明
   boolean isFair()	判断是否是公平锁
   ReentrantLock(boolean fair)	构造函数指定公平/非公平
