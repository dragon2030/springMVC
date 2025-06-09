package springStudy.beanLifeCycle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author: bigDragon
 * @create: 2025/5/6
 * @Description:
 */
@Slf4j
@Component
public class BeanLifeCycleTestApplicationContextAware implements ApplicationContextAware {
    @Override
    public void setApplicationContext (ApplicationContext applicationContext) throws BeansException {
        log.info("【ApplicationContextAware的实现类执行方法setApplicationContext】");
    }
}
