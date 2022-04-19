package com.bigDragon.javase.faseToObject.interfasce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bigDragon
 * @create 2021-01-11 15:32
 */
public interface A {
    static final Logger logger = LoggerFactory.getLogger(A.class);
    public default void a(){
        System.out.println("这是A");
        logger.info("A接口中的日志打印");
    }
}
