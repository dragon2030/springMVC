package com.bigDragon.javase.reflect.caseRecord.SyncFieldSpecialCharacterAppear;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;

/**
 * @author: bigDragon
 * @create: 2022/11/25
 * @Description:
 *  先锋ibS系统，同步bs系统时String属性会出现带有/u0000的字符
 */
public class SyncServiceImpl {
    @Test
    public void test_address() {
        String template = this.readerTemplate("D:\\documentOperation\\txt\\test_meter.txt");
        System.out.println(template);
        SyncBaseMeterDTO result = JSONObject.parseObject(template,SyncBaseMeterDTO.class);
//                JSONUtil.toBean(template, new TypeReference<Result<SyncBaseAddressDTO>>() {
//        }, true);
//        SyncBaseAddressDTO result = syncBaseAddressDTOResult.getResult();
        System.out.println("************************转为对象");
        System.out.println(result);
        System.out.println("************************对象循环处理/0问题");
        result.changFieldsToNoBlank();
        System.out.println(result);
        System.out.println("************************JSON.toJSONString，null值去除");
        System.out.println(JSON.toJSONString(result));
    }
    //同步时处理字符串中的/u0000测试用
    public String readerTemplate(String pathname) {
        FileReader fr = null;
        StringBuffer sqlTemplate = new StringBuffer();
        try {
            File file = new File(pathname);
            fr = new FileReader(file);

            char[] cbuffer = new char[1024];
            int len;
            while ((len = fr.read(cbuffer)) != -1) {
                String str = new String(cbuffer, 0, len);
                sqlTemplate.append(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //4.资源的关闭
                if (fr != null)
                    fr.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return sqlTemplate.toString();
    }
}
