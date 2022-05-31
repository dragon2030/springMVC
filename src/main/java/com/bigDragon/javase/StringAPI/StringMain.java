package com.bigDragon.javase.StringAPI;

/**
 * @author: bigDragon
 * @create: 2022/5/29
 * @Description:
 */
public class StringMain {
    //将字符串中的转义\加上
    public static void main(String[] args) {

        String str="{\"info\": [\"1、本人已知晓燃气工程项目规范相关要求\", \"2、本着“选择自由，自愿购买”原则，以上所有收费单价已进行价格公示， 本人自愿选择并购买以上收费产品\", \"3、一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十\"]}";

        System.out.println(str);
        String replace = str.replace("\"", "\\\"");
        System.out.println(replace);
    }
}
