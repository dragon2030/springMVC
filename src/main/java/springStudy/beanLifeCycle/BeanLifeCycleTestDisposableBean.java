package springStudy.beanLifeCycle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

/**
 * @author: bigDragon
 * @create: 2025/5/6
 * @Description:
 */
@Slf4j
@Component
public class BeanLifeCycleTestDisposableBean implements DisposableBean {
    @Override
    public void destroy () throws Exception {
        log.info("【BeanLifeCycleTestDisposableBean的实现类执行方法destroy】");
    }
}
