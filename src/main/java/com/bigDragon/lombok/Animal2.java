package com.bigDragon.lombok;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: bigDragon
 * @create: 2022/12/20
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//不写默认为false，当该值为 true 时，对应字段的 setter 方法调用后，会返回当前对象。
@Accessors(chain = true)
public class Animal2 {
    private String id;
    private String name;
    private Integer age;
    private String masterName;
}
