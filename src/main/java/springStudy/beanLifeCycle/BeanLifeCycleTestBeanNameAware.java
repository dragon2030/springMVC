package springStudy.beanLifeCycle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Component;

/**
 * @author: bigDragon
 * @create: 2025/5/6
 * @Description:
 */
@Slf4j
@Component
public class BeanLifeCycleTestBeanNameAware implements BeanNameAware {
    @Override
    public void setBeanName (String s) {
        log.info("【BeanNameAware的实现类执行方法setBeanName】");
    }
}
