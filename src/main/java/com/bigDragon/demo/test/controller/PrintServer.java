package com.bigDragon.demo.test.controller;/*package com.bigDragon.com.bigDragon.common.demo.controller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

*//**
 * @author 1903
 *
 *//*

@Component
public class PrintServer {
	
	    @PostConstruct
	    public void start() throws Exception {
	    	System.out.println("@PostConstruct开启");
	        new Thread(new Runnable() {
	            @Override
	            public void run() {
	                System.out.println("线程开启");
	            }
	        }).start();

	    }
        
	    @PreDestroy 
	    public void destroy() {
	    	System.out.println("@PreDestroy开启");
	    }
}
*/