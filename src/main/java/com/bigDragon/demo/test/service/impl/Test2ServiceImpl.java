package com.bigDragon.demo.test.service.impl;

import com.bigDragon.demo.test.service.Test2Service;
import com.bigDragon.demo.test.service.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: bigDragon
 * @create: 2022/5/12
 * @Description:
 */
@Service
public class Test2ServiceImpl implements Test2Service {
    @Resource
    TestService testService;


}
