package com.bigDragon.demo.test.dao;

import com.bigDragon.demo.test.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: bigDragon
 * @create: 2022/7/14
 * @Description:
 */
@Repository
public interface UserDao {
    List<User> selectAll();
    
}
