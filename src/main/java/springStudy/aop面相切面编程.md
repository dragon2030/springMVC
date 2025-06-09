**AOP (Aspect-Oriented Programming，面向切面编程) 是 Spring 框架的另一个核心特性，它允许开发者将横切关注点（如日志、事务、安全等）与业务逻辑分离，提高代码的模块化程度。**
# 基本概念
## 核心术语
* 切面 (Aspect)：横跨多个类的关注点的模块化（如日志模块）
* 连接点 (Join Point)：程序执行过程中的一个点（如方法调用、异常抛出）
* 通知 (Advice)：在特定连接点执行的动作（如"在方法调用前记录日志"）
* 切入点 (Pointcut)：匹配连接点的表达式，确定哪些连接点会触发通知
* 引入 (Introduction)：向现有类添加新方法或属性
* 目标对象 (Target Object)：被一个或多个切面通知的对象
* AOP 代理 (AOP Proxy)：由 AOP 框架创建的对象，用于实现切面契约
## Spring AOP 实现方式
* Spring AOP 主要通过动态代理实现：
  * JDK 动态代理：基于接口的代理（默认方式）
  * CGLIB 代理：基于类继承的代理（当目标类没有实现接口时使用）
## 通知类型
* Spring AOP 支持五种通知类型：
  * 前置通知 (Before Advice)：在方法执行前执行
  * 后置通知 (After Returning Advice)：在方法成功执行后执行
  * 异常通知 (After Throwing Advice)：在方法抛出异常后执行
  * 最终通知 (After (Finally) Advice)：在方法执行后执行（无论成功或失败）
  * 环绕通知 (Around Advice)：包围方法调用，可以在方法前后自定义行为
