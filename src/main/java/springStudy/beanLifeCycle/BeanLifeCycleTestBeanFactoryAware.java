package springStudy.beanLifeCycle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

/**
 * @author: bigDragon
 * @create: 2025/5/6
 * @Description:
 */
@Slf4j
@Component
public class BeanLifeCycleTestBeanFactoryAware implements BeanFactoryAware {
    @Override
    public void setBeanFactory (BeanFactory beanFactory) throws BeansException {
        log.info("【BeanFactoryAware的实现类执行方法setBeanFactory】");
    }
}
