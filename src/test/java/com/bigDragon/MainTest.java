package com.bigDragon;

import com.bigDragon.demo.test.controller.TestContorller;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: bigDragon
 * @create: 2021/12/10
 * @Description:
 */
/*@RunWith(SpringRunner.class)
@SpringBootTest*/
public class MainTest {

    @Autowired
    private TestContorller testContorller;

    @Test
    public void test01(){
        String s = testContorller.ajaxtest1("12");
        System.out.println(s);
    }

}
