package springStudy.beanLifeCycle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author: bigDragon
 * @create: 2025/5/6
 * @Description:
 */
@Slf4j
@Component
public class BeanLifeCycleTestBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization (Object bean, String beanName) throws BeansException {
        if(bean instanceof BeanLifeCycleTestBean){
            log.info("【BeanPostProcessor的实现类执行方法postProcessBeforeInitialization】bean名称: {}, 类型: {}", beanName, bean.getClass().getName());
        }
        // 必须返回bean对象（可以修改或包装后返回）
        return bean;
    }
    
    @Override
    public Object postProcessAfterInitialization (Object bean, String beanName) throws BeansException {
        if(bean instanceof BeanLifeCycleTestBean){
            log.info("【BeanPostProcessor的实现类执行方法postProcessAfterInitialization】bean名称: {}, 类型: {}", beanName, bean.getClass().getName());
        }
               // 必须返回bean对象（可以修改或包装后返回）
        return bean;
    }
}
