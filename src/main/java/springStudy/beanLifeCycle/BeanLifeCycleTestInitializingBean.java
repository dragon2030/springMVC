package springStudy.beanLifeCycle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author: bigDragon
 * @create: 2025/5/6
 * @Description:
 */
@Slf4j
@Component
public class BeanLifeCycleTestInitializingBean implements InitializingBean {
    @Override
    public void afterPropertiesSet () throws Exception {
        log.info("【InitializingBean的实现类执行方法afterPropertiesSet】");
    }
}
