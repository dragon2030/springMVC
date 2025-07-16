# Java BIO (Blocking I/O) 

Java BIO (Blocking I/O) 是 Java 传统的 I/O 编程模型，它是一种同步阻塞的 I/O 模型，在 Java 1.4 引入 NIO 之前是 Java 网络编程的主要方式。

## 基本概念

BIO 即 Blocking I/O，阻塞式 I/O，其主要特点包括：

- **同步阻塞**：当线程执行读/写操作时，线程会被阻塞，直到数据准备好或写入完成
- **一个连接一个线程**：每个客户端连接都需要一个独立的线程进行处理
- **简单直观**：编程模型简单，易于理解

## 核心组件

Java BIO 主要涉及以下类和接口：

1. **ServerSocket**：服务器端套接字，用于监听客户端连接
2. **Socket**：客户端套接字，用于建立与服务器的连接
3. **InputStream/OutputStream**：用于读写数据的输入输出流

# 套接字（ServerSocket/Socket）

代码地址

com.bigDragon.javase.ioStream.BIO.InetAddress

## 工作流程

### 服务器端典型流程

1. 创建 `ServerSocket` 并绑定端口
2. 调用 `accept()` 方法阻塞等待客户端连接
3. 当有客户端连接时，`accept()` 返回一个 `Socket` 对象
4. 为每个客户端连接创建新线程处理 I/O 操作
5. 通过 `Socket` 的输入输出流进行读写操作
6. 关闭连接

### 客户端典型流程

1. 创建 `Socket` 并指定服务器地址和端口
2. 通过 `Socket` 获取输入输出流
3. 使用流进行数据读写
4. 关闭连接

## 代码示例

### 服务器端代码

```java
public class BioServer {
    public static void main(String[] args) throws IOException {
        // 1. 创建ServerSocket并绑定端口
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("服务器启动，监听8080端口...");
        
        while (true) {
            // 2. 阻塞等待客户端连接
            Socket socket = serverSocket.accept();
            System.out.println("接收到客户端连接: " + socket.getRemoteSocketAddress());
            
            // 3. 为每个客户端创建新线程处理
            new Thread(() -> {
                try {
                    // 4. 获取输入流
                    InputStream in = socket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    
                    // 5. 获取输出流
                    OutputStream out = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(out, true);
                    
                    String request;
                    // 6. 读取客户端数据
                    while ((request = reader.readLine()) != null) {
                        System.out.println("接收到客户端消息: " + request);
                        // 7. 向客户端发送响应
                        writer.println("服务器响应: " + request);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
```

### 客户端代码

```
public class BioClient {
    public static void main(String[] args) throws IOException {
        // 1. 创建Socket并连接服务器
        Socket socket = new Socket("localhost", 8080);
        
        // 2. 获取输出流
        OutputStream out = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(out, true);
        
        // 3. 获取输入流
        InputStream in = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        
        // 4. 发送数据到服务器
        writer.println("Hello, Server!");
        
        // 5. 读取服务器响应
        String response = reader.readLine();
        System.out.println("服务器响应: " + response);
        
        // 6. 关闭连接
        socket.close();
    }
}
```

# 输入输出流(InputStream/OutputStream)

I/O是Input/Output的缩写，I/O技术用于处理设备之间的数据传输。
Java程序中，对于数据的输入/输出操作以"流(stream)"的方式进行
java.io包下提供了各种“流”类和接口，用以获取不同种类的数据，并通过标准的方法输入或输出数据。

## 一、流的分类：

按操作数据单位不同分为：字节流、字符流
按照数据流的流向不同分为：输入流，输出流
按流的角色的不同分为：节点流，处理流

输入流中分为字节流InputStream 字符流Reader
输出流中分为字节流OutputStream 字符流Writer
Java中BIO流共涉及40多个类，都是从4个抽象基类派生。
由这四个类派生处理的子类名称都以其父类名作为子类名的后缀

## 二、流的体系结构
|抽象基类|节点流（或文件流）|缓冲流（处理流的一种）|转换流（处理流的一种）|对象流（处理流的一种）|
|----|---|---|---|---|
|InputStream|FileInputStream|BufferedInputStream||ObjectInputStream|
|OutputStream|FileOutputStream|BufferedOutputStream||ObjectOutputStream|
|Reader|FileReader|BufferedReader|InputStreamReader||
|Writer|FileWriter|BufferedWriter|OutputStreamWriter||

## 三、File类

**1.File类的构造器**
File(String filePath)
File(String partentPath,String childPath)
File(File parentFile,String childPath)
**2.路径的分类**
相对路径：相较于某个路径下，指明的路径
绝对路径：包含盘符在内的文件或文件目录的路径
**3.路径分隔符**
windows和DOS系统默认使用“\”类表示
UNIX和URL使用“/”来表示
**4.File类的常用方法：**【省略】

## 四、节点流（或文件流）

* 1.对于文本文件(.txt,.java,.c,.cpp)，使用字符流FileReader/FileWriter处理
* 2.对于非文本文件(.jpg,.mp3,.mp4,.avi,.doc)，使用字节流FileInputStream/FileOutputStream处



###  FileReader/FileWriter文件字符流

示例-文件复制

```
public void testFileReaderFileWriter(){
    //2.创建输入流和输出流的对象
    FileReader fileReader = null;
    FileWriter fileWriter = null;

    try {
        //1.创建File类的对象，指明读入和写出的文件
        File srcfile = new File("src\\Main\\resources\\file\\hello.txt");
        File destfile = new File("src\\Main\\resources\\file\\hello3.txt");
        fileReader = new FileReader(srcfile);
        fileWriter = new FileWriter(destfile);
        //3.数据的读入和写出操作
        char[] cbuf = new char[5];//开发中char数组长度一般取1024
        int len;//记录每次读入到cbuf数组中字符的个数
        while ((len = fileReader.read(cbuf)) != -1) {
            //测试使用在控制台打印byte
            for(char c:cbuf){
                System.out.print(c+" ");
                System.out.println((int)c);
            }
            //每次写入len个字符
            fileWriter.write(cbuf, 0, len);
        } 
    } catch (Exception e) {
        e.printStackTrace();
    }finally{
        try {
            //4，关闭资源
            fileWriter.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        try {
            fileReader.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
```

#### Java字符流的本质

Java字符流（如Reader/Writer及其子类）的本质是：

1. **字符与字节的桥梁**：在底层I/O操作基于字节的情况下，提供字符级别的操作
2. **编码/解码器**：自动处理字符编码转换，将字符与字节序列相互转换
3. **缓冲机制**：通常包含缓冲功能以提高效率

#### Java对多字节UTF-8字符的读写处理

当处理超过2字节的UTF-8字符时：

1. **读取过程（InputStreamReader）**：
   - 自动检测并组合多个字节为一个完整的Unicode字符
   - 内部会读取足够数量的字节来构成完整字符
   - 例如一个3字节的中文字符会被完整读取，不会被截断
2. **写入过程（OutputStreamWriter）**：
   - 将单个Unicode字符自动拆分为正确的UTF-8多字节序列
   - 透明处理所有长度（1-4字节）的UTF-8编码
3. **内部处理机制**：
   - 使用CharsetDecoder/CharsetEncoder进行编码转换
   - 维护中间状态以处理跨缓冲区的多字节字符

### FileInputStream/FileOutputStream文件字节流

#### 示例-文件字节流

```
   /**
    * 非文本文件的字节流输入输出（复制）
    * @throws IOException
    */
   public void testFileInputStreamFileOutputStream(){
      //2.创建输入流和输出流的对象
      FileInputStream fileInputStream = null;
      FileOutputStream fileOutputStream = null;

      try {
         //1.创建File类的对象，指明读入和写出的文件
         File srcfile = new File("src\\Main\\resources\\jpg\\0509-01.jpg");//图像文件
         File destfile = new File("src\\Main\\resources\\jpg\\0509-02.jpg");//图像文件
         //File srcfile = new File("src\\Main\\resources\\mp3\\Laser Blast.mp3");//音频文件
         //File destfile = new File("src\\Main\\resources\\mp3\\Laser Blast2.mp3");//音频文件
         fileInputStream = new FileInputStream(srcfile);
         fileOutputStream = new FileOutputStream(destfile);
         //3.数据的读入和写出操作
         byte[] buffer = new byte[1024];//开发中byte数组长度一般取1024
         int len;//记录每次读入到buffer数组中字符的个数
         while ((len = fileInputStream.read(buffer)) != -1) {
            //测试使用在控制台打印byte
//          for(byte b:buffer){
//             System.out.print(b+" ");
//          }
//          String str = new String(buffer, 0, len);
//          System.out.println(str);
            //每次写入len个字节
            fileOutputStream.write(buffer, 0, len);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }finally{
         try {
            //4，关闭资源
            fileInputStream.close();
         } catch (Exception e2) {
            e2.printStackTrace();
         }
         try {
            fileOutputStream.close(); // close()会自动调用flush()
         } catch (Exception e2) {
            e2.printStackTrace();
         }
      }
   }
```

示例-图片异或加密解密

> 异或加密原理：
>
> * 假设有一个字节的数据是A，用密钥B进行异或，得到A^B。然后再用B异或一次的话，(A^B)^B就等于A^(B^B)，而B^B的结果是0，所以A^0就是A本身。所以两次异或同一个密钥就能恢复原始数据。

```
public void test1(String srcStr,String destStr){
   FileInputStream fiStream = null;
   FileOutputStream foStream = null;
      
   try {
      fiStream = new FileInputStream(srcStr);
      foStream = new FileOutputStream(destStr);
      byte[] buffer = new byte[1024];
      int len;
      while ((len = fiStream.read(buffer)) != -1) {
         //字节数组进行修改
         for (int i = 0; i < len; i++) {
            buffer[i] = (byte) (buffer[i] ^ 5);
         }
         foStream.write(buffer, 0, len);
      } 
   } catch (Exception e) {
      e.printStackTrace();
   }finally{
      try {
         foStream.close();
      } catch (Exception e2) {
         e2.printStackTrace();
      }
      try {
         fiStream.close();
      } catch (Exception e2) {
         e2.printStackTrace();
      }
   }

}
```

## 五、处理流

处理流的定义：处理流就是“套接”在已有的流的基础上 

* 缓冲流
* 转换流
* 标准的输入、输出流
* 打印流
* 数据流
* 对象流
* 随机处理对象流

### 缓冲流（处理流的一种）

作用：提供流的读取、写入的速度。提高读写速度的原因：内部提供了一个缓存区

#### 缓冲流的种类

* BufferedInputStream
* BufferedOutputStream
* BufferedReader
* BufferedWriter

#### 缓冲流的基本原理

缓冲流的工作原理是将数据先写入缓冲区中，当缓冲区满时再一次性写入文件或输出流，或者当缓冲区为空时一次性从文件或输入流中读取一定量的数据。这样可以减少系统的 I/O 操作次数，提高系统的 I/O 效率，从而提高程序的运行效率。

#### 示例-缓冲流-实现文本/非文本文件的复制

```
/**
 * 缓冲流-实现非文本文件的复制
 * @param srcStr 目标文件路径
 * @param destStr 复制后的文件路径
 */
public void bufferedInputStreamTest(String srcStr,String destStr){
   //2.2造缓冲流
   BufferedInputStream bufferedInputStream = null;
   BufferedOutputStream bufferedOutputStream = null;
   
   try {
      //1.造文件
      File srcfile = new File(srcStr);//图像文件
      File destfile = new File(destStr);//图像文件
      //2.造流
      //2.1造节点流
      FileInputStream fileInputStream = new FileInputStream(srcfile);
      FileOutputStream fileOutputStream = new FileOutputStream(destfile);
      bufferedInputStream = new BufferedInputStream(fileInputStream);
      bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
      //3.复制的细节：读取、写入
      byte[] buffer = new byte[10];//开发中byte数组长度一般取1024
      int len;
      while ((len = bufferedInputStream.read(buffer)) != -1) {
         bufferedOutputStream.write(buffer, 0, len);
         //bufferedOutputStream.flush();//手动刷新缓存区
      } 
   } catch (Exception e) {
      e.printStackTrace();
   }finally{
      //4.关闭资源
      //要求：先关闭外层的流，再关闭内层的流
      try {
         if(bufferedInputStream != null)
            bufferedInputStream.close();
      } catch (Exception e2) {
         e2.printStackTrace();
      }
      try {
         if(bufferedOutputStream != null)
            bufferedOutputStream.close();
      } catch (Exception e2) {
         e2.printStackTrace();
      }
      //说明：在关闭外层流的同事，内层流也会自动进行关闭，我们可以省略内层流关闭
      //fileInputStream.close();
      //fileOutputStream.close();
   }

}

/**
 * 使用BufferedReader和BufferedWriter实现文本文件的复制
 */
public void testBufferedReaderBufferWriter(String srcStr,String destStr){
    //创建文件和相应的流
    BufferedReader br = null;
    BufferedWriter bw = null;

    try {
        br = new BufferedReader(new FileReader(new File(srcStr)));
        bw = new BufferedWriter(new FileWriter(new File(destStr)));
        //读写操作
        //方式一：使用char[]数组
/*			char[] cbuf = new char[1024];
        int len;
        while ((len = br.read(cbuf)) != -1) {
            bw.write(cbuf, 0, len);
        } */

        //方式二:使用String
        String data;
        while((data = br.readLine()) != null){
            //方法一
//				bw.write(data+"\n");//data中不包含换行符
            //方法二
            bw.write(data);//data中不包含换行符
            bw.newLine();//提供换行的操作
        }
    } catch (Exception e) {
        e.printStackTrace();
    }finally{
        //关闭资源
        try {
            if(bw != null)
                bw.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        try {
            if(br != null)
                br.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
```

#### BufferedReader.readLine

Java缓冲流（BufferedReader）提供了readLine()方法来逐行读取数据，比read(char[])更高效，也是日常缓冲流使用最多的读取方式

**设计目的**

1. **高效性**：缓冲流的主要目的是减少I/O操作次数，提高读取效率。逐行读取比逐字符读取更高效。
2. **便利性**：处理文本数据时，按行处理是最常见的需求（如处理配置文件、日志文件等）。
3. **抽象层次**：BufferedReader属于字符流的高层抽象，提供了更符合文本处理习惯的方法。

**与字节/字符读取的区别**

- **read()**：每次读取单个字符（底层还是基于字节，但处理了字符编码）
- **read(char[] cbuf)**：读取到字符数组中
- **readLine()**：读取整行（直到遇到换行符或EOF）

**为什么不是默认按字节/字符读取**

1. **性能考虑**：频繁的小数据量读取（如逐字节）会导致大量系统调用，性能低下。
2. **实用场景**：大多数文本处理场景中，按行处理更有意义。
3. **缓冲机制**：即使使用readLine()，底层仍然是缓冲读取（默认8KB缓冲区），然后从缓冲区中解析出行。

**底层实现**

实际上，readLine()内部仍然是通过读取字符实现的，但它会：

1. 从缓冲区读取字符
2. 检查换行符(\n, \r或\r\n)
3. 将一行内容返回给调用者

### 转换流

处理流之二：转换流

简介：在 Java I/O 中，转换流（Conversion Streams）主要用于在字节流和字符流之间进行转换，同时可以指定字符编码格式。核心的转换流类是 InputStreamReader 和 OutputStreamWriter。

#### 转换流的种类

*     InputStreamReader：将字节的输入流转换为字符的输入流
*     OutputStreamWriter：将字符的输出流转换为字节的输出流

#### 转换流的作用

转换流的作用就是在字节流和字符流之间搭桥，特别是处理字符编码的时候。比如，读取字节流的时候，如果用不同的字符集解码成字符，这时候就需要InputStreamReader。同样，输出的时候用OutputStreamWriter可以指定字符集编码成字节。

#### 什么时候需要用到转换流

在处理文件的时候遇到了乱码问题，或者需要处理不同编码的文本文件。比如，读取一个UTF-8的文件，而系统默认编码是GBK，这时候用FileReader可能会出错，而应该用InputStreamReader包装FileInputStream并指定UTF-8编码。

> [字符流依赖系统默认编码](# 字符流依赖系统默认编码)

#### 为什么要用转换流？

* 避免乱码：直接使用 FileReader 或 FileWriter 会依赖系统默认编码，可能导致跨平台乱码。
* 编码可控：通过 InputStreamReader 和 OutputStreamWriter 显式指定编码（如 UTF-8、GBK）。
* 灵活适配：在处理网络传输、HTTP 请求等场景时，字节流和字符流需按需转换。

#### 示例-转换流

```
//使用转换流将文件复制
public void characterConvert(String srcStr,String destStr){
   InputStreamReader isr = null;
   OutputStreamWriter osw = null;

   try {
      //1.创建文件、创建流
      File file1 = new File(srcStr);
      File file2 = new File(destStr);
      FileInputStream fis = new FileInputStream(file1);
      FileOutputStream fos = new FileOutputStream(file2);
      //读取文件（字节流 → 字符流）
      isr = new InputStreamReader(fis, "utf-8");
      //写入文件（字符流 → 字节流）
      osw = new OutputStreamWriter(fos, "gbk");
      //2.读写过程
      char[] cbuf = new char[20];
      int len;
      while ((len = isr.read(cbuf)) != -1) {
         osw.write(cbuf, 0, len);
      }
   } catch (Exception e) {
      e.printStackTrace();
   }finally{
      //3.关闭资源
      try {
         if(isr != null)
            isr.close();
      } catch (Exception e2) {
         e2.printStackTrace();
      }
      try {
         if(osw != null)
            osw.close();
      } catch (Exception e2) {
         e2.printStackTrace();
      }
   }

}
```

### **标准的输入、输出流**

* System.in:标准的输入流，默认从键盘输入
* System.out: 标准的输出流，默认从控制台输出
* Scanner

### **打印流**

* PritStream和PrintWriter

### **数据流**

* 指 DataInputStream 和 DataOutputStream，用于按基本数据类型（如 int、double、String）读写二进制数据。
  *      直接操作原始数据类型（如 writeInt(int)、readDouble()）。
  *     数据以紧凑的二进制格式存储，适合高效读写。
  *     不涉及对象序列化，仅处理简单数据。
* 数据流的限制
  * 必须严格匹配读写顺序：读取数据的顺序必须与写入顺序完全一致，否则会抛出异常。


#### 示例-数据流

```
/**
 * 数据流：输出
 */
public void DataStream1(String outPrintPath){
   DataOutputStream dos = null;
   
   try {
      dos = new DataOutputStream(new FileOutputStream(outPrintPath));
      dos.writeUTF("Jack");
      dos.flush();//刷新操作，将内存中数据写入页面
      dos.writeInt(23);
      dos.flush();
      dos.writeBoolean(true);
      dos.flush();
   } catch (Exception e) {
      e.printStackTrace();
   }finally{
      try {
         if(dos != null)
            dos.close();
      } catch (Exception e2) {
         e2.printStackTrace();
      }
   }
}

	/**
	 * 数据流：输出
	 * 将文件中存储的基本类型数据变量和字符串读取到内存中，保存在变量
	 * 注意点：不同类型的数据的顺序要与当初写入文件时，保存的数据顺序一致
	 */
	public void DataStream2(String inputPath){
		DataInputStream dis = null;
		
		try {
			dis = new DataInputStream(new FileInputStream(inputPath));
			String name = dis.readUTF();
			int age = dis.readInt();
			Boolean isMale = dis.readBoolean();
			System.out.println("name:" + name);
			System.out.println("age:" + age);
			System.out.println("isMale:" + isMale);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(dis != null)
					dis.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}	
		}
	}
```

#### 对象流和数据流 核心区别与选择依据

**核心区别**

|特性|数据流（Data Streams）|对象流（Object Streams）|
|----|---|----|
|处理数据类型|基本类型（int, double, String）|对象（需实现 Serializable 接口）|
|数据格式|紧凑的二进制，无类型元信息|包含类元数据，支持对象结构重建|
|性能|更高（轻量级，直接操作原始数据）|较低（涉及序列化/反序列化开销）|
|灵活性|需手动管理数据类型顺序和格式|自动处理对象关联和类型信息|
|安全性|无序列化漏洞风险|需防范反序列化攻击（如恶意对象注入）|
|典型场景|配置文件、简单数据传输|对象持久化、跨进程/网络传输复杂数据 |

**对象流和数据流 如何选择？**

* 使用数据流：
  * 处理简单的基本类型数据。
  * 需要高性能或紧凑的二进制格式。
  * 无需保存对象关联关系。
* 使用对象流：
  * 需要保存或传输整个对象及其关联结构。
  * 数据包含复杂类型（如集合、嵌套对象）。
  * 接受序列化带来的性能和安全性代价。

### 对象流

* 对象流用于存储和读取基本数据类型数据或对象的处理流。它的强大之处就是可以把Java中的对象写入到数据源中，也能把对象从数据源中还原回来

#### 核心类

ObjectInputStream 和 ObjectOutputStream

* ObjectInputStream.readObject
* ObjectOutputStream.writeObject

#### 序列化与反序列化

* 序列化：用ObjectOutputStream类保存基本类型数据或对象的机制

* 反序列化：用ObjectInputStream类读取基本类型数据或对象的机制

* ObjectInputStream和ObjectOutputStream不会自动序列化static变量（因为它们是类级别的）和transient修饰的成员变量（这是关键字的设计用途）。对于不需要序列化的非静态成员变量，应该显式地用transient修饰。

  示例代码说明：

  ```
  class Example implements Serializable {
      static int classVar;    // 不会被序列化（因为是static）
      transient int sensitiveData; // 不会被序列化（因为是transient）
      int normalVar;          // 会被序列化
  }
  ```

#### 序列化和反序列化的过程

*  序列化的过程:将内存中的java对象保存到磁盘中或通过网络传输出去---使用ObjectOutputStream实现反序列化过程：将磁盘文件中的对象还原为内存中的一个java对象--使用ObjectInputStream

#### 序列化对象的要求

 如果需要放某个对象支持序列化机制，则必须让对象锁属的类及其属性是可序列化的，为了让某个类是可序列化的。

* 该类必须实现如下两个接口之一
  * Serializable
  * Externalizable （不常用）
* 类提供一个全局常量：serialVersionUID 区别于别的序列化对象，必加。
  * 如果没有显示定义这个静态变量，它的值是Java运行时环境根据类的内部细节自动生成的，当类的实例变量做了修改（即使是空格或字段顺序改变），serialVersionUID可能发生变化。
  * Java的序列化机制是通过在运行时判断类的serialVersionUID来验证版本一致的。
* 除了当前Person类需要实现Serializable接口之外，还必须保证其内部所有属性也必须是可序列化的。（默认情况下，基本数据类型可序列化）

#### 示例-对象流

```
	/**
	 * 序列化的过程:将内存中的java对象保存到磁盘中或通过网络传输出去---使用ObjectOutputStream实现
	 */
	public void testObjectOutputStream(String outputPath) {
		//造流造对象
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(outputPath));
			//数据写入
			//oos.writeObject(new String("金卡智能"));
			User user = new User();
			user.setName("Jack");
			user.setAge("25");
			user.setPeopleDes("very nice");
			user.setSexId("1");
			oos.writeObject(user);
			
			oos.flush();//刷新操作
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				//关闭流对象
				if(oos != null)
					oos.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * 反序列化过程：将磁盘文件中的对象还原为内存中的一个java对象--使用ObjectInputStream
	 */
	public void testObjectInputStream(String importPath) {
		ObjectInputStream ois = null;
		
		try {
			ois = new ObjectInputStream(new FileInputStream(importPath));
			Object obj = ois.readObject();
			//String str = (String) obj;
			User user = (User)obj;
			System.out.println(user.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(ois != null)
					ois.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
```

### 随机处理对象流【跳】

RandomAccessFile 是 Java 中用于对文件进行随机访问的类。与普通的输入输出流不同，RandomAccessFile 允许在文件中任意位置读写数据。

* [学习博客](https://blog.csdn.net/qq_66345100/article/details/134629811) RandomAccessFile学习笔记 实现了断点续传功能（后面有用到可以搭建）

# BIO 的优缺点

## 优点

1. **编程简单**：模型直观，易于理解和实现
2. **适合连接数少**：在连接数较少的情况下性能可以接受
3. **调试方便**：线程模型清晰，便于调试

## 缺点

1. **线程资源消耗大**：每个连接需要一个独立线程，线程创建和切换开销大
2. **并发能力有限**：受限于线程数，无法支持高并发连接
3. **资源浪费**：连接空闲时线程仍然被占用
4. **扩展性差**：当连接数增加时，性能急剧下降

## 适用场景

1. 连接数较少且固定的应用
2. 对延迟要求不高的应用
3. 简单的客户端/服务器应用
4. 需要快速开发原型的情况

# 性能优化

虽然 BIO 本身性能有限，但可以通过以下方式优化：

1. **线程池**：使用线程池管理处理线程，避免频繁创建销毁线程

   ```
ExecutorService threadPool = Executors.newFixedThreadPool(100);
   while (true) {
    Socket socket = serverSocket.accept();
       threadPool.execute(new Handler(socket));
}
   ```

2. **连接池**：复用客户端连接，减少连接建立开销

3. **伪异步 I/O**：将请求放入队列，由线程池异步处理

# BIO与 NIO/AIO 对比

| 特性     | BIO              | NIO                  | AIO                      |
| :------- | :--------------- | :------------------- | :----------------------- |
| 模型     | 同步阻塞         | 同步非阻塞           | 异步非阻塞               |
| 线程要求 | 一个连接一个线程 | 一个线程处理多个连接 | 回调机制，不需要额外线程 |
| 复杂度   | 低               | 中                   | 高                       |
| 吞吐量   | 低               | 高                   | 高                       |
| 适用场景 | 连接数少         | 连接数多，短连接     | 连接数多，长连接         |

# 其他

## 字符流依赖系统默认编码

是的，直接使用 `FileReader` 和 `FileWriter` 会依赖系统默认编码，这是一个需要注意的问题。以下是详细分析：

**1. FileReader/FileWriter 的编码依赖问题**

- `FileReader` 和 `FileWriter` **没有提供指定编码的构造函数**，它们会使用JVM的默认字符编码，这取决于运行时的系统环境578。
- 这种设计可能导致跨平台兼容性问题，比如在Windows和Linux之间迁移代码时出现乱码89。

**2. Windows和Linux的默认编码**

- **Windows**：
  - 中文系统默认使用`GBK`（或`GB2312`）编码96。
  - 新版本Windows（如Windows 10 1803+）可能部分支持UTF-8作为系统默认编码，但传统应用仍普遍依赖本地代码页（如GBK）6。
- **Linux**：
  - 现代Linux系统（如CentOS、Ubuntu等）默认使用`UTF-8`编码39。
  - 这是Unicode的一种可变长编码方式，兼容ASCII但支持多语言字符6。

**3. 潜在问题与解决方案**

- **问题表现**：

  - 在Windows开发的GBK编码文件，到Linux用`FileReader`读取可能乱码（反之亦然）9。
  - 工具如FindBugs会警告"Found reliance on default encoding"58。

- **推荐解决方案**：

  - 使用`InputStreamReader`和`OutputStreamWriter`替代，明确指定编码（如UTF-8）：

    ```
    // 读取示例（显式指定UTF-8）
    Reader reader = new InputStreamReader(new FileInputStream("file.txt"), StandardCharsets.UTF_8);
    // 写入示例
    Writer writer = new OutputStreamWriter(new FileOutputStream("file.txt"), StandardCharsets.UTF_8);
    ```

**4. 其他注意事项**

- **换行符差异**：Windows用`\r\n`，Linux用`\n`，但Java字符流会统一处理为`\n`610。
- **BOM问题**：Windows的UTF-8文件可能含BOM头（EF BB BF），而Linux通常不带，可能影响文件开头解析63。

总结：**强烈建议避免直接使用`FileReader/FileWriter`处理多语言文本**，优先使用能指定编码的`InputStreamReader/OutputStreamWriter`以确保跨平台一致性
