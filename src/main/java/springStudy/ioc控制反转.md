**IOC (Inversion of Control，控制反转) 是 Spring 框架的核心概念之一，它是一种设计思想，将对象的创建、依赖关系的管理从程序内部转移到外部容器来完成**
# 基本概念
## 控制反转 (IOC)
传统应用程序中，对象自己控制其依赖对象的创建和生命周期。而在 IOC 模式下，这种控制权被反转，交给外部容器(在 Spring 中就是 IOC 容器)来管理。
## 依赖注入 (DI)
DI (Dependency Injection，依赖注入) 是 IOC 的一种实现方式，容器负责将依赖关系注入到对象中，而不是由对象自己创建依赖。

# Spring IOC 容器
* Spring 提供了两种 IOC 容器实现：
  * BeanFactory：基础容器，提供基本的依赖注入支持
  * ApplicationContext：扩展了 BeanFactory，提供更多企业级功能，如：
    * 国际化支持
    * 事件发布
    * 资源访问
    * AOP 集成









