package com.bigDragon.testMain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: bigDragon
 * @create: 2022/4/8
 * @Description:
 */
@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private String name;
    private int sort;

}
