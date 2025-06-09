package springStudy.beanLifeCycle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author: bigDragon
 * @create: 2025/5/6
 * @Description:
 */
@Slf4j
public class BeanLifeCycleTestBean {
    private String name;
    
    public String getName () {
        return name;
    }
    
    public void setName (String name) {
        log.info("【bean对象属性装配setName方法执行】");
        this.name = name;
    }
    
    public void initMethod () {
        log.info("【bean对象自定义init-method执行】");
    }
    public void destroyMethod() {
        log.info("【bean对象自定义destroy-method执行】");
    }
    
    public BeanLifeCycleTestBean () {
        log.info("【bean对象无参构造器执行】");
    }
}
