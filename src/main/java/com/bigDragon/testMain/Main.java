package com.bigDragon.testMain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bigDragon.util.SnowFlakeUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author bigDragon
 * @create 2020-12-09 20:31
 */
public class Main {
        private static Map<String, Thread> taskThreadMap = new ConcurrentHashMap<>();
        
        public static void main(String[] args) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("1");
        }

}
