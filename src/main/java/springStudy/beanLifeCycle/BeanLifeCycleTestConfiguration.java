package springStudy.beanLifeCycle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author: bigDragon
 * @create: 2025/5/6
 * @Description:
 */
@Configuration
@Slf4j
public class BeanLifeCycleTestConfiguration {
    @PostConstruct
    public void postConstructExecute(){
        log.info("【执行带有@PostConstruct注解的方法】");
    }
    
    @PreDestroy
    public void PreDestroyExecute(){
        log.info("【执行带有@PreDestroy注解的方法】");
    }
}
