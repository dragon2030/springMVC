package com.bigDragon.jsonParse.dto;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class Data {
    private String content;
    private Long moTime;
    private String msgId;
    private String phone;

}
