package springStudy.beanLifeCycle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author: bigDragon
 * @create: 2025/5/6
 * @Description:
 */
@Controller
public class BeanLifeCycleTestController {
    @Autowired
    BeanLifeCycleTestBean beanLifeCycleTestBean;
    
}
