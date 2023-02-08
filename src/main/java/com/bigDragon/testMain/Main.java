package com.bigDragon.testMain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigDragon.util.SnowFlakeUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author bigDragon
 * @create 2020-12-09 20:31
 */
public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        String file1Name ="sys_dict_470000_470005.yml";
        int i1 = file1Name.lastIndexOf("_");
        String substring = file1Name.substring(0, i1);
        System.out.println(substring);
        int i = substring.lastIndexOf("_");
        String substring2 = file1Name.substring(0, i);
        System.out.println(substring2);
//        String substring = file1Name.substring(0,  );
//        System.out.println(substring);
    }

}
