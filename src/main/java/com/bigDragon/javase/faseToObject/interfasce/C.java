package com.bigDragon.javase.faseToObject.interfasce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bigDragon
 * @create 2021-01-11 15:36
 */
public class C implements A{
    static final Logger logger = LoggerFactory.getLogger(C.class);
    public void a(){
        System.out.println("这是C");
        logger.info("C实现类中的日志打印");
    }
}
