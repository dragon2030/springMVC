package com.bigDragon.javase.serializableTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.*;

/**
 * @author: bigDragon
 * @create: 2025/3/28
 * @Description:
 * 参考博客：https://blog.csdn.net/ZHI_YUE/article/details/125124161?ops_request_misc=%257B%2522request%255Fid%2522%253A%25225358f23c19f1596bd5ba5bf7fa6c56d9%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=5358f23c19f1596bd5ba5bf7fa6c56d9&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~sobaiduend~default-2-125124161-null-null.142^v102^pc_search_result_base4&utm_term=serializable%E6%8E%A5%E5%8F%A3%E7%9A%84%E4%BD%BF%E7%94%A8&spm=1018.2226.3001.4187
 * 
 * 自动生成 VS 手动指定 SERIALVERSIONUID
 * 如果没有显式声明 serialVersionUID，编译器会在运行时基于类的各种属性自动生成该值1。然而，这种自动计算的方式可能会因为细微的变化
 *      （如字段顺序调整、方法签名修改等）而导致不同的 serialVersionUID 值，从而引发兼容性问题。因此，推荐手动设置 serialVersionUID 来确保一致性。
 */
public class SerializableTest {
    public static void main(String[] args)  throws Exception{
        System.out.println("--------");
        Student student = new Student("xiaoming",18,"0207");
        File file = new File("src/Resource/file11.txt");
        System.out.println("--------");
        
        //写入对象
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        fos = new FileOutputStream(file);
        oos = new ObjectOutputStream(fos);
        Student person = new Student("tom", 22,"0702");
        System.out.println(person);
        oos.writeObject(person);
        oos.close();
        fos.close();
        
        //读出对象
        FileInputStream fis = null;
        fis = new FileInputStream(file);
        ObjectInputStream ois = null;
        ois = new ObjectInputStream(fis);
        Student person_read = null;
        person_read = (Student)ois.readObject();
        System.out.println(person_read);
        ois.close();
        fis.close();
        
    }
}

@Data
@AllArgsConstructor
@ToString
class Student implements Serializable {
    // 如果将 implements Serializable 去掉，将无法实现序列化 此时运行程序，报java.io.NotSerializableException异常
    private static final long serialVersionUID = 1L;
    private String name;
    private int age;
    private String classname;
}
