package com.bigDragon.javase.ioStream;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传
 * 参考文献：
 * https://juejin.im/post/6844903810079391757#comment 上传文件multipart/form-data深入解析
 * https://www.cnblogs.com/liubin1988/p/8003848.html  java文件上传和下载
 * @author bigDragon
 * @create 2020-10-07 18:34
 */
@WebServlet(name = "uploadDemo",urlPatterns = "/servlet/UploadDemo")
public class uploadDemo extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        //得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
        String savePath = this.getServletContext().getRealPath("/WEB-INF/upload");
        File file = new File(savePath);
        if(!file.exists() && !file.isDirectory()){
            System.out.println("目录或文件不存在！");
            file.mkdir();
        }
        //消息提示
        String message = "";
        //创建输入流
        BufferedInputStream bufferedInputStream = null;
        //创建输出流
        BufferedOutputStream bufferedOutputStream = null;
        try {
            //使用Apache文件上传组件处理文件上传步骤：
            //1、创建一个DiskFileItemFactory工厂
            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            //2.创建一个文件上传解析器
            ServletFileUpload fileUpload = new ServletFileUpload(diskFileItemFactory);
            //解决上传文件名的中文乱码
            fileUpload.setHeaderEncoding("UTF-8");
            //3.判断提交上来的数据是否是上传表单的数据
            if (!fileUpload.isMultipartContent(req)) {
                //按照传统方式获取数据
                return;
            }
            //使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
            List<FileItem> list = fileUpload.parseRequest(req);
            for (FileItem item : list) {
                //如果fileItem中封装的是普通输入项的数据
                if (item.isFormField()) {
                    String name = item.getFieldName();
                    String value = item.getString("UTF-8");
                    String value2 = new String(name.getBytes("iso8859-1"), "UTF-8");
                    System.out.println("name: " + name + "value: " + value + "value2: " + value2);
                } else {
                    //如果fileItem中封装的是上传文件，得到上传的文件名称
                    String fileName = item.getName();
                    System.out.println("上传文件名称为：" + fileName);
                    if (fileName == null || fileName.trim().equals("")) {
                        continue;
                    }
                    //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
                    fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);
                    //原文件名后缀
                    String suffix = fileName.substring(fileName.lastIndexOf("."));
                    //原文件名前缀
                    String prefix = fileName.substring(0,fileName.lastIndexOf("."));
                    //产生一个uuid随机文件名
                    String uuid = UUID.randomUUID().toString();
                    //完整保存文件路径
                    String fullPath=savePath+File.separator+prefix+uuid+suffix;


                    //获取item中上传文件的输入流
                    InputStream InputStream = item.getInputStream();
                    //创建文件输出流
                    FileOutputStream fileOutputStream = new FileOutputStream(fullPath);
                    //创建缓冲流
                    bufferedInputStream = new BufferedInputStream(InputStream);
                    bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                    byte buffer[] = new byte[1024];
                    int length = 0;
                    while ((length = bufferedInputStream.read(buffer)) > 0) {
                        bufferedOutputStream.write(buffer, 0, length);
                    }
                    bufferedOutputStream.flush();
                    message = "文件上传成功";
                    //删除处理文件上传时生成的临时文件
                    item.delete();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            message = "文件上传失败";
        }finally {
            //关闭流
            try {
                bufferedInputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                bufferedOutputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        req.setAttribute("message",message);
        req.getRequestDispatcher("../uploadDownload/main").forward(req,resp);
    }
}
