package com.bigDragon.jsonParse.dto;

import com.bigDragon.jsonParse.dto.Data;

import java.util.List;

/**
 * @author bigDragon
 * @create 2020-12-07 20:29
 */
@lombok.Data
public class SmsZX {
    private int code;
    private String msg;
    private Data data;

    private List<Data> dataList;


}
