package com.bigDragon.javase.ioStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author bigDragon
 * @create 2020-10-09 11:33
 */
@Controller
@RequestMapping("/uploadDownload")
public class UploadDownloadController {
    private static final Logger logger = LoggerFactory.getLogger(UploadDownloadController.class);

    /**
     * 文件上传:表单提交
     */
    public void upload2(){
        new uploadDemo();
    }
    /**
     * 上传：ajax提交
     * @param request
     * @param meFile
     * @return
     */
    @RequestMapping(value = "/uploadDemo2",method = RequestMethod.POST)
    @ResponseBody
    public String upload(HttpServletRequest request, MultipartFile meFile) {
        //获得原来的文件名（含后缀名）
        String originalFilename = meFile.getOriginalFilename();
        //原文件名后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //原文件名前缀
        String prefix = originalFilename.substring(0,originalFilename.lastIndexOf("."));
        //获取保存文件路径
        String realPath = request.getServletContext().getRealPath("/WEB-INF/upload");
        //产生一个uuid随机文件名
        String uuid =UUID.randomUUID().toString();
        //完整保存文件路径
        String fullPath=realPath+File.separator+prefix+uuid+suffix;
        File file=new File(realPath);
        if (!file.exists()){
            file.mkdir();
        }

        //创建输入流
        BufferedInputStream bufferedInputStream = null;
        //创建输出流
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(meFile.getInputStream());
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(fullPath)));
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = bufferedInputStream.read(buf))>0){
                bufferedOutputStream.write(buf,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "文件上传成功";
    }

    /**
     * 上传下载显示页面
     * @param modelAndView
     * @param request
     * @return
     */
    @RequestMapping("/main")
    public ModelAndView ajaxTest(ModelAndView modelAndView,HttpServletRequest request) throws IOException {
        //获取保存文件路径
        String uploadFilePath = request.getServletContext().getRealPath("/WEB-INF/upload");
        //目标路径
        modelAndView.setViewName("ioUpload");
        //存储要下载的文件名
        List<String> fileList = new ArrayList<String>();
        File file = new File(uploadFilePath);
        if (!file.exists()){
            file.mkdir();
        }
        File[] files = file.listFiles();
        if (files != null && files.length != 0){
            for(File f : files){
                fileList.add(f.getName());
            }
        }
        modelAndView.addObject("fileList",fileList);
        return modelAndView;
    }

    /**
     * 文件阅览查看（没有设置返回格式为可下载）
     * @param request
     * @param response
     */
    @RequestMapping(value = "/download",method = RequestMethod.GET)
    @ResponseBody
    public void download(HttpServletRequest request, HttpServletResponse response){
        //创建输入流
        BufferedInputStream bufferedInputStream = null;
        //创建输出流
        BufferedOutputStream bufferedOutputStream = null;
        try {
            //得到要下载的文件名
            String filename = request.getParameter("filename");
            filename = new String(filename.getBytes("iso8859-1"),"UTF-8");
            //上传的文件都是保存在/WEB-INF/upload目录下的子目录当中
            String fileSaveRootPath = request.getServletContext().getRealPath("/WEB-INF/upload");
            //得到要下载的文件
            File file = new File(fileSaveRootPath+File.separator+filename);
            //如果文件不存在
            if(!file.exists()){
                System.out.println("目标文件已删除");
            }
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = bufferedInputStream.read(buf))>0){
                bufferedOutputStream.write(buf,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 文件下载
     * @param request
     * @param response
     */
    @RequestMapping(value = "/download2",method = RequestMethod.GET)
    @ResponseBody
    public void download2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //创建输入流
        BufferedInputStream bufferedInputStream = null;
        //创建输出流
        BufferedOutputStream bufferedOutputStream = null;
        try {
            //得到要下载的文件名
            String filename = request.getParameter("filename");
            filename = new String(filename.getBytes("iso8859-1"),"UTF-8");
            //上传的文件都是保存在/WEB-INF/upload目录下的子目录当中
            ServletContext servletContext =request.getServletContext();
            String fileSaveRootPath = servletContext.getRealPath("/WEB-INF/upload");
            //得到要下载的文件
            File file = new File(fileSaveRootPath+File.separator+filename);
            //如果文件不存在
            if(!file.exists()){
                System.out.println("目标文件已删除");
            }

            //通知浏览器以下载的方式打开
            String mimeType=servletContext.getMimeType(filename);
            response.addHeader("Content-Type",mimeType);
            //response.addHeader("Content-Type","application/octet-stream");
            response.addHeader("Content-Disposition","attachment;filename="+URLEncoder.encode(filename, "UTF-8"));
            //读取文复制到本地
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());

            byte[] bytes=new byte[1024];
            int len=0;
            while ((len=bufferedInputStream.read(bytes))!=-1){
                bufferedOutputStream.write(bytes,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
