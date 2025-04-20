# 选择建议
优先使用 ThreadPoolExecutor 的情况：
* 需要有界队列防止OOM
* 需要自定义拒绝策略
* 需要动态调整线程数（如通过 setCorePoolSize()）
* 对线程命名有特殊要求（需自定义ThreadFactory）

优先使用 Executors 的情况：
* 快速原型开发
* 简单测试场景
* 确定无界队列不会造成内存问题
* 需要标准预置配置时

# 阿里开发规约建议
《Java开发手册》强制规定：

禁止使用Executors创建线程池

必须通过ThreadPoolExecutor显式创建

原因：

newFixedThreadPool/newSingleThreadExecutor使用无界队列可能导致OOM

newCachedThreadPool/newScheduledThreadPool最大线程数为Integer.MAX_VALUE可能导致线程爆炸
