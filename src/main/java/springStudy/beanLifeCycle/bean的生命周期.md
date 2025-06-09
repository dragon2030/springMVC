**Spring Bean的生命周期指的是一个Bean从创建到销毁的整个过程，Spring框架在这个过程中的各个阶段提供了多种扩展点，允许开发者进行自定义控制。**

# 1. 实例化阶段 (Instantiation)
* Bean的实例化：Spring容器通过构造函数或工厂方法创建Bean实例
* 依赖注入：通过setter方法或字段注入完成属性赋值
# 2. 初始化阶段 (Initialization)
* BeanNameAware.setBeanName()：如果Bean实现了BeanNameAware接口，调用该方法传递Bean的ID
* BeanFactoryAware.setBeanFactory()：如果Bean实现了BeanFactoryAware接口，调用该方法传递BeanFactory
* ApplicationContextAware.setApplicationContext()：如果Bean实现了ApplicationContextAware接口，调用该方法传递ApplicationContext
* BeanPostProcessor.postProcessBeforeInitialization()：所有BeanPostProcessor的前置处理
* @PostConstruct注解方法：执行带有@PostConstruct注解的方法
* InitializingBean.afterPropertiesSet()：如果Bean实现了InitializingBean接口，调用该方法
* 自定义init-method：执行配置中指定的初始化方法
* BeanPostProcessor.postProcessAfterInitialization()：所有BeanPostProcessor的后置处理

# 3. 使用阶段 (In Use)
* Bean完全初始化后，驻留在应用上下文中，等待被使用

# 4. 销毁阶段 (Destruction)
* @PreDestroy注解方法：【注意其使用并不普遍】执行带有@PreDestroy注解的方法
* DisposableBean.destroy()：如果Bean实现了DisposableBean接口，调用该方法
* 自定义destroy-method：执行配置中指定的销毁方法

#生命周期流程图
```
实例化Bean → 填充属性 → Aware接口回调 → BeanPostProcessor前置处理 → @PostConstruct →
InitializingBean.afterPropertiesSet() → init-method → BeanPostProcessor后置处理 →
Bean就绪可用 → @PreDestroy → DisposableBean.destroy() → destroy-method
```
# 扩展点说明
* @PostConstruct/@PreDestroy：JSR-250标准注解
* init-method/destroy-method：XML或Java配置中指定的方法
# 记录一下踩过的坑
* BeanPostProcessor
  * BeanPostProcessor的实现类中重写postProcessBeforeInitialization/postProcessAfterInitialization时，idea实现的方法返回值是null，直接用会在后续spring容器中报空指针必须返回bean。
# bean的生命周期(我之前的记录)
1. Spring启动，查找并加载需要被Spring管理的bean，进行Bean的实例化
2. Bean实例化后对将Bean的引入和值注入到Bean的属性中
3. 如果Bean实现了BeanNameAware接口的话，Spring将Bean的Id传递给setBeanName()方法
4. 如果Bean实现了BeanFactoryAware接口的话，Spring将调用setBeanFactory()方法，将BeanFactory容器实例传入
5. 如果Bean实现了ApplicationContextAware接口的话，Spring将调用Bean的setApplicationContext()方法，将bean所在应用上下文引用传入进来。
6. 如果Bean实现了BeanPostProcessor接口，Spring就将调用他们的postProcessBeforeInitialization()方法。
7. 如果Bean 实现了InitializingBean接口，Spring将调用他们的afterPropertiesSet()方法。类似的，如果bean使用init-method声明了初始化方法，该方法也会被调用
8. 如果Bean 实现了BeanPostProcessor接口，Spring就将调用他们的postProcessAfterInitialization()方法。
9. 此时，Bean已经准备就绪，可以被应用程序使用了。他们将一直驻留在应用上下文中，直到应用上下文被销毁。
10. 如果bean实现了DisposableBean接口，Spring将调用它的destory()接口方法，同样，如果bean使用了destory-method 声明销毁方法，该方法也会被调用。
