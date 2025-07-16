# Java 异常处理介绍

异常处理是Java编程中非常重要的一个部分，它允许程序在遇到错误时能够优雅地处理，而不是直接崩溃。下面我将详细介绍Java异常处理机制。

## 异常的概念

异常(Exception)是指在程序执行过程中发生的意外事件，它会中断正常的指令流。Java中的异常都是`Throwable`类的子类。

## 异常体系结构

Java异常类的继承关系：

```
Throwable
├── Error (严重错误，通常不由程序处理)
└── Exception
    ├── RuntimeException (运行时异常，非受检异常)
    └── 其他Exception (受检异常)
```

> Throwable有两个直接的子类: Error、Exception。
>
> 受检异常和非受检异常都是Exception的子类

## 异常分类

1. **受检异常(Checked Exception)**
   - 必须被捕获或声明抛出
   - 继承自Exception但不继承RuntimeException
   - 示例：IOException, SQLException
2. **非受检异常(Unchecked Exception)**
   - 包括RuntimeException及其子类
   - 不强制要求处理
   - 示例：NullPointerException, ArrayIndexOutOfBoundsException
3. **错误(Error)**
   - 严重问题，应用程序通常无法处理
   - 示例：OutOfMemoryError, StackOverflowError

## 异常处理机制

### 1. try-catch-finally

```
try {
    // 可能抛出异常的代码
} catch (ExceptionType1 e1) {
    // 处理ExceptionType1类型的异常
} catch (ExceptionType2 e2) {
    // 处理ExceptionType2类型的异常
} finally {
    // 无论是否发生异常都会执行的代码
    // 常用于释放资源
}
```

### 2. throws声明

```
public void myMethod() throws IOException {
    // 方法可能抛出IOException
}
```

### 3. throw主动抛出异常

```
throw new Exception("错误信息");
```

## 常见异常示例

```
// NullPointerException
String str = null;
System.out.println(str.length());

// ArrayIndexOutOfBoundsException
int[] arr = new int[5];
System.out.println(arr[5]);

// ClassCastException
Object obj = new Integer(0);
System.out.println((String)obj);

// NumberFormatException
int num = Integer.parseInt("abc");
```

## 自定义异常

可以创建自己的异常类：

```
public class MyException extends Exception {
    public MyException() {
        super();
    }
    
    public MyException(String message) {
        super(message);
    }
}

// 使用自定义异常
public void checkAge(int age) throws MyException {
    if (age < 18) {
        throw new MyException("年龄太小");
    }
}
```

## 异常处理最佳实践

1. 只捕获你能处理的异常
2. 不要忽略异常（空的catch块）
3. 优先使用更具体的异常类型
4. 在finally块中释放资源
5. 使用try-with-resources处理资源
6. 记录异常信息（使用日志）
7. 避免在finally块中使用return语句
8. 异常应包含有意义的描述信息

## Java 7+的增强特性

1. **try-with-resources** 语法糖

   ```
   try (BufferedReader br = new BufferedReader(new FileReader(path))) {
       return br.readLine();
   }
   ```

2. **多重捕获**

   ```
   try {
       // code
   } catch (IOException | SQLException e) {
       e.printStackTrace();
   }
   ```

异常处理是编写健壮Java程序的关键部分，合理使用异常处理可以大大提高程序的可靠性和可维护性。