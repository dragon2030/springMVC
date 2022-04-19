package com.bigDragon.jsonParse.jackson;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;
import java.util.Date;

/**
 * @author: bigDragon
 * @create: 2022/2/9
 * @Description:
 *      @JSONField(format = "yyyy-MM-dd HH:mm:ss")
 *      private Date startDate;
 *      对前端传输的字符串格式进行转换
 */
@Data
public class JsonFormatTest {
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date date;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date date2;

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = "{\"date\":\"2021-02-02\",\"date2\":\"2021-02-02\"}";
        JsonFormatTest test = objectMapper.readValue(jsonStr,JsonFormatTest.class);
        System.out.println(test.getDate());
        System.out.println(test.getDate2());
    }
}
