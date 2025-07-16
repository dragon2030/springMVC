package com.bigDragon.javase.ioStream.BIO;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectRequest;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: bigDragon
 * @create: 2025/3/30
 * @Description:
 *  管道流
 *  1、使用场景:需要将OutputStream 转换为 InputStream
 *  2、使用背景：阿里oss写入文件对象，用的方法 putObject(String bucketName, String key, InputStream input)，
 *          而业务代码中用的poi导出excel文件，workbook.write(outputStream);需要转换
 *  3、核心思路：
 *  将 POI 的 OutputStream 转换为 InputStream，通过 字节数组缓冲 或 管道流（Piped Stream） 实现流的方向转换。推荐使用 
 *          字节数组缓冲（适用于中小文件）或 异步管道流（适用于大文件）。
 *          
 *  问题：字节数组缓冲法，是否可以 分块处理数据 实现管道流
 *      对于POI生成的Excel文件，可能无法在生成过程中分块上传，因为文件内容在生成过程中可能还不完整，导致上传到OSS的文件无法正确打开。
 *      OSS的putObject方法通常需要完整的InputStream
 *  
 */
public class PipedStreamTest {
    public static void main (String[] args) {
        //字节数组缓冲
        new PipedStreamTest().new OSSExcelUploader(null,null);
        //异步管道流
        new PipedStreamTest().new OSSLargeExcelUploader(null,null);
    }
    
    
    class OSSExcelUploader {
        
        private final OSS ossClient;
        private final String bucketName;
        
        public OSSExcelUploader(OSS ossClient, String bucketName) {
            this.ossClient = ossClient;
            this.bucketName = bucketName;
        }
        
        /**
         * 将 Workbook 写入 OSS
         * 注意存在问题 若 Excel 文件较大（超过 100MB），使用字节数组可能导致内存溢出。
         * @param key       OSS 对象路径（如 "reports/2024/sales.xlsx"）
         * @param workbook  POI 的 Workbook 对象
         */
        public void uploadExcelToOSS(String key, Workbook workbook) {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                // 1. POI 将 Excel 写入字节数组输出流
                workbook.write(outputStream);
                
                // 2. 将字节数组转换为输入流
                byte[] excelBytes = outputStream.toByteArray();
                try (ByteArrayInputStream inputStream = new ByteArrayInputStream(excelBytes)) {
                    
                    // 3. 上传到 OSS
                    ossClient.putObject(new PutObjectRequest(bucketName, key, inputStream));
                }
            } catch (Exception e) {
                throw new RuntimeException("上传 Excel 到 OSS 失败", e);
            }
        }
    }
    
    /**
     * 关键特性：管道流的两端（读和写）必须运行在不同的线程中，否则会导致线程阻塞。
     */
    class OSSLargeExcelUploader {
    
        private final OSS ossClient;
        private final String bucketName;
    
        public OSSLargeExcelUploader (OSS ossClient, String bucketName) {
            this.ossClient = ossClient;
            this.bucketName = bucketName;
        }
        
        public void uploadLargeExcel(String key, Workbook workbook) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            try (PipedOutputStream pos = new PipedOutputStream();
                 PipedInputStream pis = new PipedInputStream(pos)) {
            
                // 异步写入 POI 数据到管道
                executor.submit(() -> {
                    try {
                        workbook.write(pos);
                        pos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            pos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            
                // 异步上传到 OSS
                ossClient.putObject(bucketName, key, pis);
            
            } catch (Exception e) {
                throw new RuntimeException("大文件上传失败", e);
            } finally {
                executor.shutdown();
            }
        }
    }
    

}
