package com.bigDragon.dozer;

import com.bigDragon.dozer.dto.UserDto;
import com.bigDragon.dozer.uitl.BeanMapper;
import com.bigDragon.dozer.vo.UserVo;
import org.junit.Test;

/**
 * @author: bigDragon
 * @create: 2022/2/9
 * @Description:
 */
public class TestDozer {

    @Test
    public void transformInDozerWay(){
        UserDto userDto = new UserDto(1, "Mike", 22);
        UserVo userVo = BeanMapper.map(userDto, UserVo.class);
        System.out.println(userVo);
    }
}
