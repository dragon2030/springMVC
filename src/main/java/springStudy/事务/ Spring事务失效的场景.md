#概述
* 大多数Spring Boot项目只需要在方法上标记@Transactional注解，即可一键开启方法的事务性配置。
* 但是，事务如果没有被正确出，很有可能会导致事务的失效，带来意想不到的数据不一致问题
# 事务失效场景
## 方法访问权限问题
* 非public方法：Spring事务默认只对public方法生效
# final/static方法
* final/static方法：无法被代理，导致事务失效
## 自调用问题
```java
@Service
public class UserService {
    public void updateUser() {
        this.insertUser(); // 自调用，事务失效
    }
    
    @Transactional
    public void insertUser() {
        // 插入操作
    }
}
```
# 异常处理不当
* 捕获异常不抛出：吞掉了异常，事务不会回滚
* 抛出非RuntimeException：默认只回滚RuntimeException
```
@Transactional
public void method() {
    try {
        // 业务代码
    } catch (Exception e) {
        // 捕获但不抛出，事务不会回滚
    }
}
```
# 数据库引擎不支持
使用MyISAM等不支持事务的存储引擎
# 传播行为设置不当
使用了PROPAGATION_NOT_SUPPORTED等不支持事务的传播行为
# 类未被Spring管理
* 没有使用@Service、@Component等注解
# 未配置开启事务
* 如果项目中没有配置Spring的事务管理器，即使使用了Spring的事务管理功能，Spring的事务也不会生效
# 多线程调用/异步方法
* 在@Async方法上使用@Transactional通常无效
* 方法内创建线程
> 在实际项目开发中，多线程的使用场景还是挺多的。如果Spring事务用在多线程场景中使用不当，也会导致事务无法生效。
