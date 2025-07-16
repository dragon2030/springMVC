[][][][][][][][][][][][]

[视频地址](https://www.bilibili.com/video/BV1iq4y1u7vj?spm_id_from=333.788.videopod.episodes&vd_source=64c1ffcfbcba0d5d4324f915391bb7a3&p=97)

在本机部署了mysql8.0 windonws服务mysql

同时本机部署了mysql5.7 windows服务mysql57

部署mysql5.7的时候遇到了环境问题，[mysql57部署记录](D:\Program Files\mysql-5.7.44-winx64\mysql5.7部署记录.txt)

#   第01章_Linux下MySQL的安装与使用

  讲师：尚硅谷-宋红康（江湖人称：康师傅）

# 1.安装前说明

##  1.1 Linux系统及工具的准备

*  安装并启动好两台虚拟机： CentOS 7

*  掌握克隆虚拟机的操作

  > [Linux虚拟机的克隆](D:\private_file\Mysql\资料01-3-Linux虚拟机的克隆.docx)

* 需要对克隆后的虚拟机进行修改

  *  mac地址

  *  主机名

  * ip地址

  * UUID

*  安装有 Xshell 和 Xftp 等访问CentOS系统的工具

*  CentOS6和CentOS7在MySQL的使用中的区别

>     1. 防火墙：6是iptables，7是firewalld
>
>     2. 启动服务的命令：6是service，7是systemctl

##   1.2 查看是否安装过MySQL

* 如果你是用rpm安装, 检查一下RPM PACKAGE：

 ``` 
 rpm -qa | grep -i mysql # -i 忽略大小写
 ```

*  检查mysql service：

```
  systemctl status mysqld.service
```

* 如果存在mysql-libs的旧版本包，显示如下：

[][][][][][][][][][][][][][]![image-20250710105114877](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250710105114877.png)

* 如果不存在mysql-lib的版本，显示如下：

![image-20250710105133069](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250710105133069.png)

###   1.3 MySQL的卸载

  1. **关闭 mysql 服务**

```shell
systemctl stop mysqld.service
```

  2. **查看当前 mysql 安装状况**

```  shell
rpm -qa | grep -i mysql
# 或
yum list installed | grep mysql
```

  3. **卸载上述命令查询出的已安装程序**

```shell
  yum remove mysql-xxx mysql-xxx mysql-xxx mysqk-xxxx
```

  务必卸载干净，反复执行 rpm -qa | grep -i mysql 确认是否有卸载残留

  4. **删除 mysql 相关文件**

  查找相关文件

```shell
  find / -name mysql
```

  删除上述命令查找出的相关文件

```shell
  rm -rf xxx
```

  **5.删除 my.cnf**

> 是mysql配置文件 在window环境下叫my.ini

```shell
  rm -rf /etc/my.cnf
```

## 2.MySQL的Linux版安装

###   2.1 MySQL的4大版本

> * **MySQL Community Server 社区版本**，开源免费，自由下载，但不提供官方技术支持，适用于大多数普通用户。
>
> * **MySQL Enterprise Edition  企业版本**，需付费，不能在线下载，可以试用30天。提供了更多的功能和更完备的技术支持，更适合于对数据库的功能和可靠性要求较高的企业客户。
>
> * **MySQL  Cluster 集群版**，开源免费。用于架设集群服务器，可将几个MySQL Server封装成一个Server。需要在社区版或企业版的基础上使用。
> *  **MySQL** Cluster CGE 高级集群版**，需付费。

* 截止目前，官方最新版本为 8.0.27 。此前，8.0.0 在 2016.9.12日就发布了。

* 本课程中主要使用 8.0.25版本。同时为了更好的说明MySQL8.0新特性，还会安装 MySQL5.7 版本，作为对比。

  此外，官方还提供了 MySQL Workbench （GUITOOL）一款专为MySQL设计的
  ER/数据库建模工具  。它是著名的数据库设计工具DBDesigner4的继任者。MySQLWorkbench又分为两个版本，分别是  社区版（MySQL Workbench OSS）、 商用版 （MySQL WorkbenchSE）。

###   2.2 下载MySQL指定版本

#### **1.下载地址**

  官网：https://www.mysql.com

#### 2.打开官网，点击DOWNLOADS

  然后，点击 MySQL Community(GPL) Downloads

![image-20250710110742990](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250710110742990.png)

#### **3.点击 MySQL Community Server**

![image-20250710111214293](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250710111214293.png)

#### **4.在General Availability(GA) Releases中选择适合的版本**

  如果安装Windows 系统下MySQL ，推荐下载 MSI安装程序 ；点击 Go to
  Download Page 进行下载即可

![image-20250710110823415](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250710110823415.png)

* Windows下的MySQL安装有两种安装程序

  * mysql-installer-web-community-8.0.25.0.msi  下载程序大小：2.4M；安装时需要联网安装组件。

  * mysql-installer-community-8.0.25.0.msi  下载程序大小：435.7M；安装时离线安装即可。推荐。

#### **5. Linux系统下安装MySQL的几种方式**

##### **5.1 Linux系统下安装软件的常用三种方式：**

  **方式1：rpm命令**

  使用rpm命令安装扩展名为".rpm"的软件包。[][][][][][][][][][]

  .rpm包的一般格式：

![image-20250710111019644](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250710111019644.png)

  **方式2：yum命令**

  需联网，从 **互联网获取** 的yum源，直接使用yum命令安装。

  **方式3：编译安装源码包**(视频中使用的方式)
  针对 tar.gz  这样的压缩格式，要用tar命令来解压；如果是其它压缩格式，就使用其它命令。

#####   **5.2 Linux系统下安装MySQL，官方给出多种安装方式**

| 安装方式       | 特点                                                 |
| -------------- | ---------------------------------------------------- |
| rpm            | 安装简单，灵活性差，无法灵活选择版本、升级           |
| rpm repository | 安装包极小，版本安装简单灵活，升级方便，需要联网安装 |
| 通用二进制包   | 安装比较复杂，灵活性高，平台通用性好                 |
| 源码包         | 安装最复杂，时间长，参数设置灵活，性能好             |

* 这里不能直接选择CentOS 7系统的版本，所以选择与之对应的 Red Hat
    Enterprise Linux 
* https://downloads.mysql.com/archives/community/直接点Download下载RPM
    Bundle全量包。包括了所有下面的组件。不需要一个一个下载了。

![image-20250710111530340](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250710111530340.png)

#### 6.下载的tar包，用压缩工具打开

![image-20250710111607989](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250710111607989.png)

*  解压后rpm安装包 （红框为抽取出来的安装包）

  ![image-20250710111617747](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250710111617747.png)

[][][][][][][][][][]

###   2.3 CentOS7下检查MySQL依赖

#### 1.检查/tmp临时目录权限（必不可少）

  由于mysql安装过程中，会通过mysql用户在/tmp目录下新建tmp_db文件，所以请给/tmp较大的权限。执行  ：

```shell
  chmod -R 777 /tmp
```

![image-20250710111714458](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250710111714458.png)

#### 2.安装前，检查依赖

```shell
  rpm -qa|grep libaio  
```

如果存在libaio包如下：

![image-20250710111754464](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250710111754464.png)

```shell
  rpm -qa|grep net-tools
```

  如果存在net-tools包如下：

![image-20250710111831604](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250710111831604.png)

```shell
  rpm -qa|grep net-tools
```

* 如果不存在需要到centos安装盘里进行rpm安装。安装linux如果带图形化界面，这些都是安装好的。

###   2.4 CentOS7下MySQL安装过程

##### 1.将安装程序拷贝到/opt目录下

  在mysql的安装文件目录下执行：（必须按照顺序执行,有前后依赖，不顺序会报错）

> rpm -ivh mysql-community-common-8.0.25-1.el7.x86_64.rpm
>
>  rpm -ivh mysql-community-client-plugins-8.0.25-1.el7.x86_64.rpm
>
>   rpm -ivh mysql-community-libs-8.0.25-1.el7.x86_64.rpm
>
>   rpm -ivh mysql-community-client-8.0.25-1.el7.x86_64.rpm
>
>   rpm -ivh mysql-community-server-8.0.25-1.el7.x86_64.rpm

* 注意:  如在检查工作时，没有检查mysql依赖环境在安装mysql-community-server会报错
* rpm 是Redhat Package  Manage缩写，通过RPM的管理，用户可以把源代码包装成以rpm为扩展名的文件形式，易于安装。
  *   -i , --install 安装软件包
  *   -v , --verbose 提供更多的详细信息输出
  * -h , --hash 软件包安装的时候列出哈希标记 (和 -v   一起使用效果更好)，展示进度条

##### 2.安装过程截图

![image-20250710112207503](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250710112207503.png)

  安装过程中可能的报错信息：

![image-20250710112217770](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250710112217770.png)

>  一个命令：yum remove mysql-libs 解决，清除之前安装过的依赖即可

[][][][][][][][][][][][][][][][]

##### 3.查看MySQL版本

  执行如下命令，如果成功表示安装mysql成功。类似java  -version如果打出版本等信息

  ```
  mysql --version
   #或
  mysqladmin --version
  ```

  执行如下命令，查看是否安装成功。需要增加 -i
  不用去区分大小写，否则搜索不到。

```
  rpm -qa|grep -i mysql
```

![image-20250710112322347](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250710112322347.png)

##### 4.服务的初始化

  为了保证数据库目录与文件的所有者为 mysql 登录用户，如果你是以 root
  身份运行 mysql 服务，需要执行下面的命令初始化：

```
  mysqld --initialize --user=mysql
```

> **MySQL 初始化的作用**
>
> MySQL 安装后的初始化过程主要完成以下关键任务：
>
> 1. **创建系统数据库**：初始化会创建 `mysql`、`performance_schema` 和 `sys` 等系统数据库
> 2. **生成 root 账户**：建立初始的 root 用户账户（除非使用 `--insecure` 选项）
> 3. **设置数据目录结构**：按照配置创建完整的数据文件目录结构
> 4. **生成临时密码**：安全模式下会为 root 账户生成临时随机密码（显示在控制台或错误日志中）
> 5. **初始化系统表**：填充授权表、时区表等系统表

说明： --initialize 选项默认以“安全”模式来初始化，则会为 root  用户生成一个密码并将 该密码标记为过期  ，登录后你需要设置一个新的密码。生成的 临时密码 会往日志中记录一份。

  查看密码：

```
  cat /var/log/mysqld.log
```

![image-20250710112713789](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250710112713789.png)

  root@localhost: 后面就是初始化的密码

##### 5.启动MySQL，查看状态

  ```shell
  #加不加.service后缀都可以
  
    启动：systemctl start mysqld.service
  
    关闭：systemctl stop mysqld.service
  
    重启：systemctl restart mysqld.service
  
    查看状态：systemctl status mysqld.service
  ```

>  mysqld 这个可执行文件就代表着 MySQL
>   服务器程序，运行这个可执行文件就可以直接启动一个服务器进程。

![image-20250713181659078](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250713181659078.png)

  查看进程：

  ps -ef | grep -i mysql

![image-20250713181713623](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250713181713623.png)

##### 6.查看MySQL服务是否自启动

  systemctl list-unit-files|grep mysqld.service

![image-20250713181741267](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250713181741267.png)

  默认是enabled。

>   systemctl list-unit-files|grep mysqld.service这条命令用于检查 MySQL 服务的单元文件状态：
>
> **命令分解**
>
> 1. **`systemctl list-unit-files`**
>    列出所有 systemd 管理的服务单元文件（包括已启用/禁用的服务）。
> 2. **`grep mysqld.service`**
>    过滤出与 `mysqld.service` 相关的行（MySQL 的服务名称通常为 `mysqld` 或 `mysql`）。

  如不是enabled可以运行如下命令设置自启动

  systemctl enable mysqld.service

![image-20250713181840607](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250713181840607.png)

  如果希望不进行自启动，运行如下命令设置

  systemctl disable mysqld.service

![image-20250713181854910](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250713181854910.png)

## 3. MySQL登录

###   3.1 首次登录

  通过 mysql -hlocalhost -P3306 -uroot -p 进行登录，在Enter
  password：录入初始化密码

![image-20250713182043149](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250713182043149.png)

[][][][][][]

###   3.2 修改密码

  因为初始化密码默认是过期的，所以查看数据库会报错

  修改密码：

  ALTER USER 'root'@'localhost' IDENTIFIED BY 'new_password';

5.7版本之后（不含5.7），mysql加入了全新的密码安全机制。设置新密码太简单会报错。

![image-20250713182108711](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250713182108711.png)

  改为更复杂的密码规则之后，设置成功，可以正常使用数据库了

![image-20250713182129216](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250713182129216.png)

###   3.3 设置远程登录

#### 1.**当前问题**

  在用SQLyog或Navicat中配置远程连接Mysql数据库时遇到如下报错信息，这是由于Mysql配置了不支持远程连接引起的。

![image-20250713182145255](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250713182145255.png)

[][][][][][]

#### 2.**确认网络**

  1.在远程机器上使用ping ip地址 保证网络畅通

  2.在远程机器上使用telnet命令 保证端口号开放 访问

```
  telnet ip地址 端口号
```

  拓展： telnet命令开启 :

[][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][][]

#### 3.**关闭防火墙或开放端口**

  **方式一：关闭防火墙【生产不可取】**

  CentOS6 ：

  service iptables stop

  CentOS7：

  systemctl start firewalld.service

  systemctl status firewalld.service

  systemctl stop firewalld.service

  #设置开机启用防火墙

  systemctl enable firewalld.service

  #设置开机禁用防火墙

  systemctl disable firewalld.service

  **方式二：开放端口**

  查看开放的端口号

  firewall-cmd --list-all

  设置开放的端口号

  firewall-cmd --add-service=http --permanent

  firewall-cmd --add-port=3306/tcp --permanent

  重启防火墙

  firewall-cmd --reload

#### 4.Linux下修改配置

  在Linux系统MySQL下测试：

```
  use mysql;

  select Host,User from user;
```

![image-20250713182342625](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250713182342625.png)

  可以看到root用户的当前主机配置信息为localhost。

* **修改Host为通配符%** 

 Host列指定了允许用户登录所使用的IP，比如user=root  Host=192.168.1.1。这里的意思就是说root用户只能通过192.168.1.1的客户端去访问。  user=root Host=localhost，表示只能通过本机客户端去访问。而 %是个  通配符  ，如果Host=192.168.1.%，那么就表示只要是IP地址前缀为“192.168.1.”的客户端都可以连接。如果  Host=% ，表示所有IP都有连接权限。

  注意：在生产环境下不能为了省事将host设置为%，这样做会存在安全问题，具体的设置可以根据生产环境的IP进行设置。

```
update user set host = '%' where user ='root';
```

  Host设置了“%”后便可以允许远程访问。

![image-20250713182611162](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250713182611162.png)

  Host修改完成后记得执行flush privileges使配置立即生效：

```
  flush privileges;
```

  5. 测试

*  如果是 MySQL5.7  版本，接下来就可以使用SQLyog或者Navicat成功连接至MySQL了。
* 如果是  MySQL8 版本，连接时还会出现如下问题：

![image-20250713182831231](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250713182831231.png)

  配置新连接报错：错误号码 2058，分析是 mysql 密码加密方法变了。

  **解决方法**：Linux下 mysql -u root -p 登录你的 mysql 数据库，然后
  执行这条SQL：

 ```
  ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'abc123';
 ```

  然后在重新配置SQLyog的连接，则可连接成功了，OK。

## 4.MySQL8的密码强度评估（了解）

###   4.1 MySQL不同版本设置密码(可能出现)

* MySQL5.7中：成功

```
  mysql> alter user 'root' identified by 'abcd1234';

  Query OK, 0 rows affected (0.00 sec)
```

*  MySQL8.0中：失败

```
  mysql> alter user 'root' identified by 'abcd1234'; # HelloWorld_123
  ERROR 1819 (HY000): Your password does not satisfy the current policy requirements
```



###   4.2 MySQL8之前的安全策略

  在MySQL  8.0之前，MySQL使用的是validate_password插件检测、验证账号密码强度，保障账号的安全性。

  **安装/启用插件方式1：在参数文件my.cnf中添加参数**

```
  [mysqld]

  plugin-load-add=validate_password.so

  \#ON/OFF/FORCE/FORCE_PLUS_PERMANENT:  是否使用该插件(及强制/永久强制使用)
    
  validate-password=FORCE_PLUS_PERMANENT
```

> * 说明1： plugin  library中的validate_password文件名的后缀名根据平台不同有所差异。  对于Unix和Unix-like系统而言，它的文件后缀名是.so，对于Windows系统而言，它的文件后缀名是.dll。
>
> * 说明2： 修改参数后必须重启MySQL服务才能生效。
>
> * 说明3： 参数FORCE_PLUS_PERMANENT是为了防止插件在MySQL运行时的时候被卸载。当你卸载插件时就会报错。如下所示。
>
>   ```
>     mysql> SELECT PLUGIN_NAME, PLUGIN_LIBRARY, PLUGIN_STATUS, LOAD_OPTION
>     -> FROM INFORMATION_SCHEMA.PLUGINS
>           
>     -> WHERE PLUGIN_NAME = 'validate_password';
>     +-------------------+----------------------+---------------+----------------------+
>     | PLUGIN_NAME | PLUGIN_LIBRARY | PLUGIN_STATUS | LOAD_OPTION |
>     +-------------------+----------------------+---------------+----------------------+
>     | validate_password | validate_password.so | ACTIVE |
>     FORCE_PLUS_PERMANENT |
>     +-------------------+----------------------+---------------+----------------------+
>     1 row in set (0.00 sec)
>           
>     mysql> UNINSTALL PLUGIN validate_password;
>     ERROR 1702 (HY000): Plugin 'validate_password' is force_plus_permanent
>     and can not be unloaded
>           
>     mysql>
>   ```

  **安装/启用插件方式2：运行时命令安装（推荐）**

```
  mysql> INSTALL PLUGIN validate_password SONAME 'validate_password.so';
  Query OK, 0 rows affected, 1 warning (0.11 sec)
```

  此方法也会注册到元数据，也就是mysql.plugin表中，所以不用担心MySQL重启后插件会失效。

###   4.3 MySQL8的安全策略

#### 1.validate_password说明

  MySQL  8.0，引入了服务器组件（Components）这个特性，validate_password插件已用服务器组件重新实现。8.0.25版本的数据库中，默认自动安装validate_password组件。

  **未安装插件前，执行如下两个指令** ，执行效果：

```
  mysql> show variables like 'validate_password%';

  Empty set (0.04 sec)

  mysql> SELECT * FROM mysql.component;

  ERROR 1146 (42S02): Table 'mysql.component' doesn't exist
```

  安装插件后，执行如下两个指令 ，执行效果：

```sql
mysql> SELECT * FROM mysql.component;
  +--------------+--------------------+------------------------------------+
  | component_id | component_group_id | component_urn |
  +--------------+--------------------+------------------------------------+
  | 1 | 1 | file://component_validate_password |
  +--------------+--------------------+------------------------------------+
  1 row in set (0.00 sec)
```

  ```sql
    mysql> show variables like 'validate_password%';
  
    +--------------------------------------+--------+
  
    | Variable_name | Value |
  
    +--------------------------------------+--------+
  
    | validate_password.check_user_name | ON |
  
    | validate_password.dictionary_file | |
  
    | validate_password.length | 8 |
  
    | validate_password.mixed_case_count | 1 |
  
    | validate_password.number_count | 1 |
  
    | validate_password.policy | MEDIUM |
  
    | validate_password.special_char_count | 1 |
  
    +--------------------------------------+--------+
  
    7 rows in set (0.01 sec)
  ```



  关于 **validate_password** 组件对应的系统变量说明：

| 选项                                 | 默认值 | 参数描述                                                     |
| ------------------------------------ | ------ | ------------------------------------------------------------ |
| validate_password_check_user_name    | ON     | 设置为ON的时候表示能将密码设置成当前用户名。                 |
| validate_password_dictionary_file    |        | 用于检查密码的字典文件的路径名，默认为空                     |
| validate_password_length             | 8      | 密码的最小长度，也就是说密码长度必须大于或等于8              |
| validate_password_mixed_case_count   | 1      | 如果密码策略是中等或更强的， validate_password要求密码具有的小写和大写字符的最小数量。对于给定的这个值密码必须有那么多小写字符和那么多大写字符。 |
| validate_password_number_count       | 1      | 密码必须包含的数字个数                                       |
| validate_password_policy             | MEDIUM | 密码强度检验等级，可以使用数值0、1、2或相应的符号值LOW、MEDIUM、STRONG来指定。 0/LOW ：只检查长度。1/MEDIUM ：检查长度、数字、大小写、特殊字符。 2/STRONG ：检查长度、数字、大小写、特殊字符、字典文件。 |
| validate_password_special_char_count | 1      | 密码必须包含的特殊字符个数                                   |

> 提示：
>   组件和插件的默认值可能有所不同。例如，MySQL 5.7.  validate_password_check_user_name的默认值为OFF。

#### 2.修改安全策略

  修改密码验证安全强度

```sql
  SET GLOBAL validate_password_policy=LOW;

  SET GLOBAL validate_password_policy=MEDIUM;

  SET GLOBAL validate_password_policy=STRONG;

  SET GLOBAL validate_password_policy=0; # For LOW

  SET GLOBAL validate_password_policy=1; # For MEDIUM

  SET GLOBAL validate_password_policy=2; # For HIGH

  #注意，如果是插件的话,SQL为set global validate_password_policy=LOW
```

  此外，还可以修改密码中字符的长度

 ```
  set global validate_password_length=1;
 ```

#### 3.密码强度测试

[][][][][][][][]

  如果你创建密码是遇到“Your password does not satisfy the current policy  requirements”，可以通过函数组件去检测密码是否满足条件：  0-100。当评估在100时就是说明使用上了最基本的规则：大写+小写+特殊字符+数字组成的8位以上密码

```
  mysql> SELECT VALIDATE_PASSWORD_STRENGTH('medium');

  +--------------------------------------+

  | VALIDATE_PASSWORD_STRENGTH('medium') |

  +--------------------------------------+

  | 25 |

  +--------------------------------------+

  1 row in set (0.00 sec)
```

> 25 表示不满足密码校验规则

```
  mysql> SELECT VALIDATE_PASSWORD_STRENGTH('K354*45jKd5');

  +-------------------------------------------+

  | VALIDATE_PASSWORD_STRENGTH('K354*45jKd5') |

  +-------------------------------------------+

  | 100 |

  +-------------------------------------------+

  1 row in set (0.00 sec)
```

> 100表示满足了密码校验规则

  注意：如果没有安装validate_password组件或插件的话，那么这个函数永远都返回0。
  关于密码复杂度对应的密码复杂度策略。如下表格所示：

| Password Test                             | Return Value |
| ----------------------------------------- | ------------ |
| Length < 4                                | 0            |
| Length ≥ 4 and < validate_password.length | 25           |
| Satisfies policy 1 (LOW)                  | 50           |
| Satisfies policy 2 (MEDIUM)               | 75           |
| Satisfies policy 3 (STRONG)               | 100          |

###   4.4 卸载插件、组件(了解)

  **卸载插件**

```
 mysql> UNINSTALL PLUGIN validate_password;

  Query OK, 0 rows affected, 1 warning (0.01 sec)
```

  **卸载组件**

```
  mysql> UNINSTALL COMPONENT 'file://component_validate_password';

  Query OK, 0 rows affected (0.02 sec)
```

## 5.字符集的相关操作

[][][][]

###   5.1 修改MySQL5.7字符集

#### **1. 修改步骤**

  在MySQL 8.0版本之前，默认字符集为 **latin1** ，utf8字符集指向的是 utf8mb3 。网站开发人员在数据库设计的时候往往会将编码修改为utf8字符集。如果遗忘修改默认的编码，就会出现乱码的问题。从MySQL 8.0开始，数据库的默认编码将改为 utf8mb4 ，从而避免上述乱码的问题。

#####   **操作1：查看默认使用的字符集**

```
  show variables like 'character%';
  或者
  show variables like '%char%';
```

  MySQL8.0中执行：

![image-20250715201426233](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250715201426233.png)

  MySQL5.7中执行：

![image-20250715201923758](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250715201923758.png)

  MySQL 5.7 默认的客户端和服务器都用了 latin1
  不支持中文，保存中文会报错。

  在MySQL5.7中添加中文数据时，报错

![image-20250715201946467](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250715201946467.png)

  因为默认情况下，创建表使用的是 latin1 。

![image-20250715202001030](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250715202001030.png)

#####   **操作2：修改字符集**

  vim /etc/my.cnf

  在MySQL5.7或之前的版本中，在文件最后加上中文字符集配置：

  character_set_server=utf8

[][][][][][][][]

#####   **操作3：重新启动MySQL服务**

  systemctl restart mysqld

> 但是原库、原表的设定不会发生变化，参数修改只对新建的数据库生效。

#### 2.已有库&表字符集的变更

  MySQL5.7版本中，以前创建的库，创建的表字符集还是latin1。

![image-20250715202309246](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250715202309246.png)

  修改已创建数据库的字符集

```
  alter database dbtest1 character set 'utf8';
```



  修改已创建数据表的字符集

```
  alter table t_emp convert to character set 'utf8';
```

![image-20250715202352227](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250715202352227.png)

> 注意：但是原有的数据如果是用非'utf8'编码的话，数据本身编码不会发生改变。已有数据需要导出或删除，然后重新插入。

###   5.2 各级别的字符集

  MySQL有4个级别的字符集和比较规则，分别是：

* 服务器级别

* 数据库级别

* 表级别

* 列级别

  执行如下SQL语句：

```
  show variables like 'character%';
```

[][][][][][][][][][][][]

![image-20250715202606880](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250715202606880.png) 

* character_set_server：服务器级别的字符集

* character_set_database：当前数据库的字符集

* character_set_client：服务器解码请求时使用的字符集
* character_set_connection：服务器处理请求时会把请求字符串从character_set_client转为character_set_connection
* character_set_results：服务器向客户端返回数据时使用的字符集

> character_set_server设置后会直接决定character_set_database，之后当创建数据库时，没有指明就会用这个默认的
>
> 当创建表时没有指明会用库默认字符集
>
> 创建列时没有指明字符集会和表一样也就是库的默认字符集(后面有说)

#### 1.服务器级别

*  character_set_server ：服务器级别的字符集。

  我们可以在启动服务器程序时通过启动选项或者在服务器程序运行过程中使用
  SET 语句修改这两个变量的值。比如我们可以在配置文件中这样写：

```
  [server]
  character_set_server=gbk # 默认字符集
  collation_server=gbk_chinese_ci #对应的默认的比较规则
```

  当服务器启动的时候读取这个配置文件后这两个系统变量的值便修改了。

#### 2.数据库级别

*  character_set_database ：当前数据库的字符集

  我们在创建和修改数据库的时候可以指定该数据库的字符集和比较规则，具体语法如下：

```
  CREATE DATABASE 数据库名
  [[DEFAULT] CHARACTER SET 字符集名称]
  [[DEFAULT] COLLATE 比较规则名称];
  ALTER DATABASE 数据库名
  [[DEFAULT] CHARACTER SET 字符集名称]
  [[DEFAULT] COLLATE 比较规则名称];
```

#### 3.表级别

  我们也可以在创建和修改表的时候指定表的字符集和比较规则，语法如下：

[][][][][][][][][][][][]

```
  CREATE TABLE 表名 (列的信息)
  [[DEFAULT] CHARACTER SET 字符集名称]
  [COLLATE 比较规则名称]]
  ALTER TABLE 表名
  [[DEFAULT] CHARACTER SET 字符集名称]
  [COLLATE 比较规则名称]
```

  **如果创建和修改表的语句中没有指明字符集和比较规则，将使用该表所在数据库的字符集和比较规则作为该表的字符集和比较规则。**

#### 4.列级别

  对于存储字符串的列，同一个表中的不同的列也可以有不同的字符集和比较规则。我们在创建和修改列定义的时候可以指定该列的字符集和比较规则，语法如下：

```
  CREATE TABLE 表名(
  列名 字符串类型 [CHARACTER SET 字符集名称] [COLLATE 比较规则名称],
  其他列...
  );

  ALTER TABLE 表名 MODIFY 列名 字符串类型 [CHARACTER SET 字符集名称]
  [COLLATE 比较规则名称];
```

  **对于某个列来说，如果在创建和修改的语句中没有指明字符集和比较规则，将使用该列所在表的字符集和比较规则作为该列的字符集和比较规则。**

> 提示
>
> 在转换列的字符集时需要注意，如果转换前列中存储的数据不能用转换后的字符集进行表示会发生错误。比方说原先列使用的字符集是utf8，列中存储了一些汉字，现在把列的字符集转换为ascii的话就会出错，因为ascii字符集并不能表示汉字字符。

#### 5.小结

  我们介绍的这4个级别字符集和比较规则的联系如下：

* 如果 创建或修改列 时没有显式的指定字符集和比较规则，则该列 默认用表的字符集和比较规则
* 如果 创建表时 没有显式的指定字符集和比较规则，则该表默认用数据库的 字符集和比较规则
* 如果 创建数据库时  没有显式的指定字符集和比较规则，则该数据库 默认用服务器的  字符集和比较规则

  知道了这些规则之后，对于给定的表，我们应该知道它的各个列的字符集和比较规则是什么，从而根据这个列的类型来确定存储数据时每个列的实际数据占用的存储空间大小了。比方说我们向表
  t 中插入一条记录：

```
  mysql> INSERT INTO t(col) VALUES('我们');
  Query OK, 1 row affected (0.00 sec)
  mysql> SELECT * FROM t;
  +--------+
  | s |
  +--------+
  | 我们 |
  +--------+
  1 row in set (0.00 sec)
```



[][][][][][][][][][][][][][][][][][][][][][][][][][]

  首先列 col 使用的字符集是 gbk ，一个字符 '我' 在 gbk 中的编码为 0xCED2  ，占用两个字节，两个字符的实际数据就占用4个字节。如果把该列的字符集修改为  utf8 的话，这两个字符就实际占用6个字节

###   5.3 字符集与比较规则(了解)

#### 1.utf8 与 utf8mb4

  utf8  字符集表示一个字符需要使用1～4个字节，但是我们常用的一些字符使用1～3个字节就可以表示了。而字符集表示一个字符所用的最大字节长度，在某些方面会影响系统的存储和性能，所以设计MySQL的设计者偷偷的定义了两个概念：

* utf8mb3 ：阉割过的 utf8 字符集，只使用1～3个字节表示字符。

  > 就是mysql5.7中的utf8

* utf8mb4 ：正宗的 utf8 字符集，使用1～4个字节表示字符。

> 这个比较就是主要区别

#### 2.比较规则

  上表中，MySQL版本一共支持41种字符集，其中的 **Default collation**  列表示这种字符集中一种默认的比较规则，里面包含着该比较规则主要作用于哪种语言，比如  **utf8_polish_ci** 表示以波兰语的规则比较， **utf8_spanish_ci**  是以西班牙语的规则比较， **utf8_general_ci** 是一种通用的比较规则。

> ![image-20250715204518033](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250715204518033.png)
>
> 最后一列Maxlen代表最大字节数

  后缀表示该比较规则是否区分语言中的重音、大小写。具体如下：

| 后缀 | 英文释义           | 描述             |
| ---- | ------------------ | ---------------- |
| _ai  | accent insensitive | 不区分重音       |
| _as  | accent sensitive   | 区分重音         |
| _ci  | case insensitive   | 不区分大小写     |
| _cs  | case sensitive     | 区分大小写       |
| _bin | binary             | 以二进制方式比较 |

  最后一列 Maxlen ，它代表该种字符集表示一个字符最多需要几个字节。

  **常用操作1：**

```
  #查看GBK字符集的比较规则
  SHOW COLLATION LIKE 'gbk%';

  #查看UTF-8字符集的比较规则
  SHOW COLLATION LIKE 'utf8%';
```

![image-20250715230457093](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250715230457093.png)

![image-20250715230512833](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250715230512833.png)



  **常用操作2：**

```
  #查看服务器的字符集和比较规则
  SHOW VARIABLES LIKE '%_server';

  #查看数据库的字符集和比较规则
  SHOW VARIABLES LIKE '%_database';

  #查看具体数据库的字符集
  SHOW CREATE DATABASE dbtest1;

  #修改具体数据库的字符集
  ALTER DATABASE dbtest1 DEFAULT CHARACTER SET 'utf8' COLLATE
  'utf8_general_ci';
```

说明1:

> utf8_unicode_ci和utf8_general_ci对中、英文来说没有实质的差别。
>
> utf8_general_ci 校对速度快，但准确度稍差。
>
> utf8_unicode_ci 准确度高，但校对速度稍慢,
> 一般情况，用utf8_general_ci就够了，但如果你的应用有德语、法语或者俄语，请一定使用utf8_unicode_ci。

说明2：

> 修改了数据库的默认字符集和比较规则后，原来已经创建的表格的字符集和比较规则并不会改变，如果需
> 要，那么需单独修改。

  **常用操作3：**

```
  #查看表的字符集
  show create table employees;

  #查看表的比较规则
  show table status from atguigudb like 'employees';

  #修改表的字符集和比较规则
  ALTER TABLE emp1 DEFAULT CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';
```



###   5.4 请求到响应过程中字符集的变化

我们知道从客户端发往服务器的请求本质上就是一个 字符串 ，服务器向客户端返回的结果本质上也是一个字符串，而字符串其实是使用某种字符集编码的二进制数据。这个字符串可不是使用一种字符集的编码方式一条道走到黑的，从发送请求到返回结果这个过程中伴随着多次字符集的转换，在这个过程中会用到3个系统变量，我们先把它们写出来看一下:


| 系统变量                 | 描述                                                         |
| ------------------------ | ------------------------------------------------------------ |
| character_set_client     | 服务器解码请求时使用的字符                                   |
| character_set_connection | 服务器处理请求时会把请求字符串从 character_set_client 转为 character_set_connection |
| character_set_results    | 服务器向客户端返回数据时使用的字符集                         |

这几个系统变量在我的计算机上的默认值如下（不同操作系统的默认值可能不同）：

![image-20250715232747456](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250715232747456.png)

以下是个示例具体说明三者之间的关系：

  为了体现出字符集在请求处理过程中的变化，我们这里特意修改一个系统变量的值：

```
  mysql> set character_set_connection = gbk;
  Query OK, 0 rows affected (0.00 sec)
```

  现在假设我们客户端发送的请求是下边这个字符串：

```
  SELECT * FROM t WHERE s = '我';
```

  为了方便大家理解这个过程，我们只分析字符 '我'
  在这个过程中字符集的转换。现在看一下在请求从发送到结果返回过程中字符集的变化：

  1.  客户端发送请求所使用的字符集

     一般情况下客户端所使用的字符集和当前操作系统一致，不同操作系统使用的字符集可能不一样，如下：

     * 类 Unix 系统使用的是 utf8

     * Windows 使用的是 gbk

  当客户端使用的是 utf8 字符集，字符 '我'在发送给服务器的请求中的字节形式就是：0xE68891

>  提示
>
>  如果你使用的是可视化工具，比如navicat之类的，这些工具可能会使用自定义的字符集来编码发送到服务器的字符串，而不采用操作系统默认的字符集（所以在学习的时候还是尽量用命令行窗口）。

2.  服务器接收到客户端发送来的请求其实是一串二进制的字节，它会认为这串字节采用的字符集是  character_set_client ，然后把这串字节转换为 character_set_connection  字符集编码的 字符。

     由于我的计算机上 character_set_client 的值是 utf8 ，首先会按照 utf8  字符集对字节串0xE68891 进行解码，得到的字符串就是 '我' ，然后按照
     character_set_connection 代表的字符集，也就是 gbk  进行编码，得到的结果就是字节串 0xCED2 。

  3. 因为表 t 的列 col 采用的是 gbk 字符集，与 character_set_connection一致，所以直接到列 中找字节值为 0xCED2 的记录，最后找到了一条记录。

>  提示
>
>   如果某个列使用的字符集和character_set_connection代表的字符集不一致的话，还需要进行一次字符集转换。

    4. 上一步骤找到的记录中的 col 列其实是一个字节串 0xCED2 ， col列是采用 gbk 进行编码的，所 以首先会将这个字节串使用 gbk进行解码，得到字符串 '我' ，然后再把这个字符串使用character_set_results 代表的字符集，也就是 utf8进行编码，得到了新的字节串： 0xE68891 ，然后发送给客户端。



    5. 由于客户端是用的字符集是 utf8 ，所以可以顺利的将 0xE68891解释成字符 我 ，从而显示到我们的显示器上，所以我们人类也读懂了返回的结果。

  总结图示如下：

![image-20250716001405613](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250716001405613.png)

**经验：**

开发中通常把 character_set_client 、character_set_connection、character_set_results 这三个系统变量设置成和客户端使用的字符集一致的情况，这样减少了很多无谓的字符集转换。为了方便我们设置，MySQL提供了一条非常简便的语句:

```
SET NAMES 字符集名；
```

这一条语句产生的效果和我们执行这3条的效果是一样的:

```
SET character_set_client =字符集名;
SET character_set_connection =字符集名;
SET character_set_results =字符集名;
```

比方说我的客户端使用的是utf8字符集，所以需要把这几个系统变量的值都设置为utf8 :

```
mysql> SET NAMES utf8;
```

另外，如果你想在启动客户端的时候就把character_set_client、character_set_connection 、character_set_results这三个系统变量的值设置成一样的，那我们可以在启动客户端的时候指定一个叫default-character-set的启动选项，比如在配置文件里可以这么写:

```
[client]
default-character-set=utf8
```

它起到的效果和执行一遍SET NAMES utf8是一样一样的，都会将那三个系统变量的值设置成utf8。

## 6. SQL大小写规范(了解)

[][][][][][]

### 6.1 Windows和Linux平台区别

  在 SQL 中，关键字和函数名是不用区分字母大小写的，比如  SELECT、WHERE、ORDER、GROUP BY 等关键字，以及 ABS、MOD、ROUND、MAX
  等函数名。

  不过在 SQL 中，你还是要确定大小写的规范，因为在 Linux 和 Windows  环境下，你可能会遇到不同的大小写问题。 windows系统默认大小写不敏感
  ，但是 linux系统是大小写敏感的 。

  通过如下命令查看：

```
  SHOW VARIABLES LIKE '%lower_case_table_names%'
```

* Windows系统下：\

![image-20250715233402072](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250715233402072.png)

* Linux系统下：

![image-20250715233525647](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250715233525647.png)

* lower_case_table_names参数值的设置：

  * 默认为0，大小写敏感 。

  * 设置1，大小写不敏感。创建的表，数据库都是以小写形式存放在磁盘上，对于sql语句都是转换为小写对表和数据库进行查找。

  * 设置2，创建的表和数据库依据语句上格式存放，凡是查找都是转换为小写进行。

* 两个平台上SQL大小写的区别具体来说：

>  MySQL在Linux下数据库名、表名、列名、别名大小写规则是这样的：
>
>   1、数据库名、表名、表的别名、变量名是严格区分大小写的；
>
>   2、关键字、函数名称在 SQL 中不区分大小写；
>
>   3、列名（或字段名）与列的别名（或字段别名）在所有的情况下均是忽略大小写的；
>
>   **MySQL在Windows的环境下全部不区分大小写**

###   6.2 Linux下大小写规则设置（建议不改）

  当想设置为大小写不敏感时，要在 my.cnf 这个配置文件 [mysqld] 中加入  lower_case_table_names=1 ，然后重启服务器。

* 但是要在重启数据库实例之前就需要将原来的数据库和表转换为小写，否则将找不到数据库名。

* 此参数适用于MySQL5.7。在MySQL 8下禁止在重新启动 MySQL 服务时将  lower_case_table_names 设置成不同于初始化 MySQL 服务时设置的  lower_case_table_names  值。如果非要将MySQL8设置为大小写不敏感，具体步骤为：

  1、停止MySQL服务

  2、删除数据目录，即删除 /var/lib/mysql 目录（现有的初始化值给删除）

  3、在MySQL配置文件（ /etc/my.cnf ）中添加 lower_case_table_names=1

  4、启动MySQL服务

###   6.3 SQL编写建议

  如果你的变量名命名规范没有统一，就可能产生错误。这里有一个有关命名规范的建议：

  1. 关键字和函数名称全部大写；

  2. 数据库名、表名、表别名、字段名、字段别名等全部小写；

  3. SQL 语句必须以分号结尾。

  数据库名、表名和字段名在 Linux MySQL  环境下是区分大小写的，因此建议你统一这些字段的命名规则，比如全部采用小写的方式。

  虽然关键字和函数名称在 SQL  中不区分大小写，也就是如果小写的话同样可以执行。但是同时将关键词和函数名称全部大写，以便于区分数据库名、表名、字段名。

## 7.sql_mode的合理设置

### 7.1介绍

sql_mode会影响MySQL支持的SQL语法以及它执行的**数据验证检查**。通过设置sql_mode,可以完成不同严格程度的数据校验，有效地保障数据准确性。

MySQL服务器可以在不同的SQL模式下运行，并且可以针对不同的客户端以不同的方式应用这些模式，具体取决于sql_mode系统变量的值。

MySQL5.6和MySQL5.7默认的sql_mode模式参数是不一样的:

* 5.6的mode默认值为空(即:**NO_ENGINE_SUBSTITUTION** )，其实表示的是一个空值，相当于没有什么模式设置，可以理解为**宽松模式**。在这种设置下是可以允许一些非法操作的，比如允许一些非法数据的插入。
* 5.7的mode是**STRICT_TRANS_TABLES**，也就是**严格模式**。用于进行数据的严格校验，错误数据不能插入，报error(错误)，并且事务回滚。

###   7.2 宽松模式 vs 严格模式

####   **宽松模式：**

  如果设置的是宽松模式，那么我们在插入数据的时候，即便是给了一个错误的数据，也可能会被接受，并且不报错。

  **举例**  ：我在创建一个表时，该表中有一个字段为name，给name设置的字段类型时  char(10) ，如果我在插入数据的时候，其中name这个字段对应的有一条数据的  长度超过了10  ，例如'1234567890abc'，超过了设定的字段长度10，那么不会报错，并且**取前10个字符存上**，也就是说你这个数据被存为'1234567890'，而'abc'就没有了。但是，我们给的这条数据是错误的，因为超过了字段长度，但是并没有报错，并且mysql自行处理并接受了，这就是宽松模式的效果。

  **应用场景** ：通过设置sql  mode为宽松模式，来保证大多数sql符合标准的sql语法，这样应用在不同数据库之间进行  **迁移** 时，则不需要对业务sql 进行较大的修改。

####   **严格模式：**

  出现上面宽松模式的错误，应该报错才对，所以MySQL5.7版本就将sql_mode默认值改为了严格模式。所以在  **生产和开发环境 中，我们必须采用的是严格模式**，进而 开发、测试环境  的数据库也必须要设置，这样在开发测试阶段就可以发现问题。并且我们即便是用的MySQL5.6，也应该自行将其改为严格模式。

  **开发经验**  ：MySQL等数据库总想把关于数据的所有操作都自己包揽下来，包括数据的校验，其实开发中，我们应该在自己  开发的项目程序级别将这些校验给做了  ，虽然写项目的时候麻烦了一些步骤，但是这样做之后，我们在进行数据库迁移或者在项目的迁移时，就会方便很多。

  **改为严格模式后可能会存在的问题：**

[][][][][][]

  若设置模式中包含了 NO_ZERO_DATE  ，那么MySQL数据库不允许插入零日期，插入零日期会抛出错误而不是警告。例如，表中含字段TIMESTAMP列（如果未声明为NULL或显示DEFAULT子句）将自动分配DEFAULT  '0000-00-00  00:00:00'（零时间戳），这显然是不满足sql_mode中的NO_ZERO_DATE而报错。

###   7.3 宽松模式再举例

  **宽松模式举例1：**

切换sql_mode为ONLY_FULL_GROUP_BY后发生报错

```
  select * from employees 
  group by department_id limit 10;
  
  set sql_mode = ONLY_FULL_GROUP_BY;
  
  select * from employees 
  group by department_id limit 10
```

![image-20250715234019558](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250715234019558.png)

  **宽松模式举例2：**

```sql
INSERT INTO mytbl2 (id,NAME,age)VALUES(6,'Tom', 'aaa' );
SELECT * FROM mytbl2;
```



![image-20250715234139714](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250715234139714.png)

![image-20250715234153547](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250715234153547.png)  

设置 sql_mode 模式为 STRICT_TRANS_TABLES ，然后插入数据：

![image-20250715234212095](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250715234212095.png)



###   7.4 模式查看和设置

* 查看当前的sql_mode

```
  select @@session.sql_mode
  select @@global.sql_mode
  #或者
  show variables like 'sql_mode';
```

* 临时设置方式：设置当前窗口中设置sql_mode

```
  SET GLOBAL sql_mode = 'modes...'; #全局
  SET SESSION sql_mode = 'modes...'; #当前会话
```

  举例：

[][][][][][][][]

>  #改为严格模式。此方法只在当前会话中生效，关闭当前会话就不生效了。
>
> set SESSION sql_mode='STRICT_TRANS_TABLES';

>  #改为严格模式。此方法在当前服务中生效，重启MySQL服务后失效。
>
>   set GLOBAL sql_mode='STRICT_TRANS_TABLES';

* 永久设置方式：在/etc/my.cnf中配置sql_mode

  在my.cnf文件(windows系统是my.ini文件)，新增：

```
  [mysqld]
  sql_mode=ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR
  _DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION
```

  然后 重启MySQL 。

  当然生产环境上是禁止重启MySQL服务的，所以采用 **临时设置方式 +  永久设置方式**  来解决线上的问题，那么即便是有一天真的重启了MySQL服务，也会永久生效了。



### 7.5 sql_mode常用值

![image-20250716003316246](https://raw.githubusercontent.com/dragon2030/springMVC/refs/heads/master/src/main/java/mysqlStudy/%E5%AD%A6%E4%B9%A0%E8%BF%87%E7%A8%8Bmysql/mysql%E5%9F%BA%E7%A1%80/assets/image-20250716003316246.png)

# 其他

## 在Linux环境下MySQL服务的名称是`mysqld`原因

在Linux环境下，MySQL服务的名称是`mysqld`（而不是`mysql`），这主要是由于历史惯例和Unix/Linux的命名规范决定的。以下是具体原因：

1. **区分客户端和服务端**

- **`mysql`**：这是MySQL的**客户端程序**（命令行工具），用于连接和操作MySQL服务器。
- **`mysqld`**（MySQL Daemon）：这是MySQL的**服务端程序**（守护进程），负责实际的数据存储、查询处理等核心功能。

Linux/Unix系统通常使用`d`后缀表示**守护进程（daemon）**，例如：

- `httpd`（Apache服务器）
- `sshd`（SSH服务端）
- `ftpd`（FTP服务端）

2. **历史惯例**

- 在Unix/Linux系统中，服务端程序通常以`d`结尾（表示"daemon"），而客户端程序不带`d`。
- MySQL延续了这一传统，因此：
  - **`mysql`** → 客户端（用户直接运行的命令）
  - **`mysqld`** → 服务端（后台运行的守护进程）

3. **Systemd服务名称**

- 在`systemd`管理的系统中，MySQL的服务名称通常是：
  - `mysqld.service`（如RHEL/CentOS）
  - 或 `mysql.service`（如Debian/Ubuntu）
- 但实际的二进制程序仍然是`mysqld`。

4. **如何确认MySQL服务名称？**

- 查看服务状态：

  ```
  systemctl status mysqld   # 在RHEL/CentOS上
  systemctl status mysql   # 在Debian/Ubuntu上
  ```

- 查看进程：

  ```
  ps aux | grep mysqld
  ```

  你会看到`/usr/sbin/mysqld`这样的进程。

5. **特殊情况**

- 某些Linux发行版（如Debian/Ubuntu）可能会使用`mysql`作为服务名，但底层仍然是`mysqld`进程。
- MariaDB（MySQL的一个分支）通常使用`mariadb.service`作为服务名。

总结

| 名称     | 用途                     | 示例命令                 |
| :------- | :----------------------- | :----------------------- |
| `mysql`  | **客户端**（命令行工具） | `mysql -u root -p`       |
| `mysqld` | **服务端**（守护进程）   | `systemctl start mysqld` |

所以，当你运行`systemctl status mysqld.service`时，实际上是在查看MySQL**服务端**的运行状态。
