package com.bigDragon.jdbc;

import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author: bigDragon
 * @create: 2022/6/23
 * @Description:
 */
public class ConnectionTest {
    //连接接口模板
    @Test
    public void testConnection1_20220623_2132() throws SQLException{
        //驱动
        Driver driver = null;

        //数据库地址
        String url = null;
        Properties info = null;

        Connection connect = driver.connect(url, info);

        System.out.println(connect);
    }

    //方式一
    @Test
    public void testConnection1_20220623() throws SQLException{
        // 获取Driver实现类对象
        Driver driver = new com.mysql.cj.jdbc.Driver();

        // jdbc:mysql:协议
        // localhost:ip地址
        // 3306：默认mysql的端口号
        // test:test数据库
        String url = "jdbc:mysql://localhost:3306/jdbc_study?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai";
        // 将用户名和密码封装在Properties中
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "root");

        Connection connect = driver.connect(url, info);

        System.out.println(connect);
        //打印输出：com.mysql.cj.jdbc.ConnectionImpl@27808f31

    }

    //java程序员中不希望直接出现第三方的api，都是sun公司提供的api，当切换到别的数据库驱动时，可以较快的切换，具有较好的移植性
    //方式二：方式一的迭代
    @Test
    public void testConnection2_20220627() throws Exception{
        // 1.获取Driver实现类对象：使用反射获取方式一种的driver实现对象
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        //反射-调用其无参构造器
        Driver driver = (Driver)clazz.newInstance();

        // 2.提供要连接的数据库
        String url = "jdbc:mysql://localhost:3306/jdbc_study?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai";

        // 3.提供连接需要的用户名和密码
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "root");

        // 4.获取连接
        Connection conn = driver.connect(url, info);
        System.out.println(conn);
    }

    //sun公司提供DriverManage来个管理驱动
    //使用DriverManage替换Driver
    //方式三：方式二的迭代
    @Test
    public void testConnection3_20220628() throws Exception{

        // 1.获取Driver实现类对象：使用反射获取方式一种的driver实现对象
        Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        //反射-调用其无参构造器
        Driver driver = (Driver)clazz.newInstance();

        // 2.提供另外三个连接的基本信息：
        String url = "jdbc:mysql://localhost:3306/jdbc_study?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai";
        String user = "root";
        String password = "root";

        //注册驱动
        DriverManager.registerDriver(driver);

        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }

    /**
     * 方式四：方式三的迭代简化
     * 较方式三可以简化 实例化驱动 注册驱动的步骤
     */
    @Test
    public void testConnection4_20220628() throws Exception{
        // 1.提供另外三个连接的基本信息：
        String url = "jdbc:mysql://localhost:3306/jdbc_study?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai";
        String user = "root";
        String password = "root";

        /**
         * 2.当加载驱动时，Driver实现类加载到内存中，此时会调用mysql的Driver实现类的静态代码快
         *  static {
         *         try {
         *             DriverManager.registerDriver(new Driver());
         *         } catch (SQLException var1) {
         *             throw new RuntimeException("Can't register driver!");
         *         }
         *     }
          */

        Class.forName("com.mysql.cj.jdbc.Driver");
        //Class clazz = Class.forName("com.mysql.cj.jdbc.Driver");
        //Driver driver = (Driver)clazz.newInstance();
        //DriverManager.registerDriver(driver);

        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }

    //方式五（final版）：将数据库连接需要的4个基本信息声明在配置文件中，通过读取配置文件的方式读取
    /*
     * 此种方式的好处？
     * 1.实现了数据与代码的分离。实现了解耦
     * 2.如果需要修改配置文件信息，可以避免程序重新打包。
     * 总结：这也是配置文件的主要作用
     */
    @Test
    public void testConnection5_20220629() throws Exception{
        /**
         * 1.读取配置文件中的4个基本信息
         * 1.1获取类加载器(应用程序类加载器)
         */
        //获取应用程序类加载器
        //ClassLoader.getSystemClassLoader()与this.getClass().getClassLoader()一样，此处都可获得系统类加载器
        ClassLoader classLoader = this.getClass().getClassLoader();
        //ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        //读取配置文件获取流
        InputStream resourceAsStream = classLoader.getResourceAsStream("jdbc.properties");

        //加载流到properties
        Properties properties = new Properties();
        properties.load(resourceAsStream);

        //获取属性值
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String driverName = properties.getProperty("driverName");

        //加载驱动
        Class.forName(driverName);

        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }
    //总结，其实五个方法的核心都是第一个方法，后续方法都是在其上按照核心步骤进行优化修改
}
