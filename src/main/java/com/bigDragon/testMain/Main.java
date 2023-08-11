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
            Student student = new Student();
            student.setName("Mike");
            Dog dog = new Dog();
            dog.setName("dog1");
            student.setDog(dog);
            String s = JSON.toJSONString(student);
            System.out.println(s);
            Student s1 = JSONObject.parseObject(s, Student.class);
            System.out.println(s1);
            Student s2 = JSONObject.parseObject(s, new TypeReference<Student>(){});
            System.out.println(s2);
        }
        
        private static void submitTask(ExecutorService executor, String taskId) {
            executor.submit(() -> {
                Thread currentThread = Thread.currentThread();
                taskThreadMap.put(taskId, currentThread);
                System.out.println("Task " + taskId + " started in thread: " + currentThread.getName());
                
                // 模拟任务执行一段时间
                try {
                    Thread.sleep(10*1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                System.out.println("Task " + taskId + " completed in thread: " + currentThread.getName());
                taskThreadMap.remove(taskId);
            });
        }
        
        private static void cancelTask(String taskId) {
            Thread thread = taskThreadMap.get(taskId);
            if (thread != null) {
                thread.interrupt();
                taskThreadMap.remove(taskId);
                System.out.println("Task " + taskId + " cancelled.");
            }
        }

}
