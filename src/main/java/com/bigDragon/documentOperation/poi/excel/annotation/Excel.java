package com.bigDragon.documentOperation.poi.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: bigDragon
 * @create: 2022/1/12
 * @Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Excel {

    //导入时，对应excel的字段，主要是用户区分每个字段，不能有annocation重名
    //导出是的列明，导出排序跟定义了annotation的字段有关
    String exportName();

    //导出时再excel中每个列的宽，单位为字符，一个汉字两个字符
    //如 以列名内容中较适合的长度，例如姓名为列6【姓名一般为3个字】 性别列4【男女占1，但列名占两个字】
    //限制1-255
    int exportFieldWidth() default 1;

    //导出时是否进行字段转换 例如性别用int存储，导出时可能转换为男，女
    //若Sign为1，则需要再pojo中加入一个方法get 字段名 Convert 例如字段sex，需要加入public String getSexCovert()
    //若是sign为0，则不生效
    @Deprecated
    int exportConvertSign() default 0;

    //导入数据是否需要转化 及 对已有的excel，是否需要将字段转为对应的数据
    //若是sign为1，则需要再pojo中加入 void set字段名Convert（String text）4
    @Deprecated
    int importConvertSign() default 0;

    /**
     * 合并单元格标志
     *
     * 若是sign为1,开启合并单元格，将相同的行合并为同一个单元格。
     * 当上一个字段也需合并单元格时，认为上个单元格为此单元格父类，会根据父单元格进行合并
     *
     * 若是sign为0，不开启合并单元格功能
     */
    int mergeCellSign() default 0;

    /**
     * 自定义格式
     */
    String selfFormat() default "yyyy-mm-dd";
}
