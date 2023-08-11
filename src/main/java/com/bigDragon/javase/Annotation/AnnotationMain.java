package com.bigDragon.javase.Annotation;

/**
 * 注解类（Annotation）
 *
 * 一、概述：
 *      1.从JDK5.0开始，Java增加了对元数据（MetaData）的支持，也就是Annotation（注解）
 *      2.Annotation其实就是代码里的特殊标记，这些标记可以在编译，类加载、运行时被读取，并执行响应的处理。通过使用Annotation，程序员可以再不改变原有逻辑的情况下，在源文件中嵌入一些补充信息。
 *      代码分析工具、开发者工具和部署工具可通过这些补充信息进行验证或者进行部署。
 *      3.Annotation可以像修饰符一样被使用，可以用于修饰包，类。构造器。方法。成员变量，参数，局部变量的声明，这些信息被保存在Annotation的“name=value”对中。
 *      4.在JavaSE中，注解的使用目的比较简单，例如编辑过时的功能，忽略警告等。在JavaEE/Android中注解占据了更重要的角色，例如用来配置应用程序的任何切面，替代JavaEE旧版中所遗留的繁冗代码和
 *      XML配置等。
 *      5.未来的开发密室都是基于注解的，JPA是基于注解的，Spring2.5以上都是基于注解的，Hibernate3.x以后也是基于注解的，现在的Struts2有一部分也是基于注解的了，注解是一种趋势，在一定程度上
 *      可以说：框架=注解+反射+设计模式
 * 二、常见的Annotation示例
 *      示例一：生成文档相关的注解：
 *          author标明代码该类模块的作者，多个作者之间使用“，”分隔
 *          version标明该类模块的版本
 *          see参数转向，也就是相关主题
 *          since从哪个版本开始增加的
 *          param对方法中某参数的说明，如果没有参数就不能写
 *          return对方法返回值的说明，如果方法的返回值类型是void就不能写
 *          exception对方法可能抛出的异常进行说明，如果方法没有用throws显示抛出的异常就不能写其中
 *      示例二：在编译时进行格式检查（JDK内置的三个基本注释）
 *          Override：限定重写父类方法，该注释只能用于方法
 *          Deprecated：用于表示所修饰的元素（类、方法等）已过时。通常是因为所修饰的结构危险或存在更好的选择
 *          SuppressWarnings:抑制编译器警告
 *      示例：跟踪代码依赖性，实现替代配置文件的功能
 *  三、如何自定义注解：（参照@SuppressWarnings定义）
 *      1.定义新的Annotation类型使用@interface关键字
 *      2.自定义注解自动继承了java.lang.annotation.Annotation接口
 *      3.Annotation的成员变量再Annotation定义中以无参的形式来什么。其方法名和返回值定义了该成员的名字和类型。我们称为配置参数。类型只能是是八种基本数据类型、String类型、Class类型、enum类型
 *      Annotation类型、以上所有类型的数组。
 *      4.可以再定义Annotation的成员变量时为其指定初始值，指定成员变量的初始值可以使用default关键字
 *      5.如果只有一个参数成员，建议使用参数名为value
 *      6.如果定义的注解含有配置参数，那么使用时必须指定参数值，除非它有默认值。格式是“参数=参数值”，如果只有一个参数成员，且名称为value，可以省略为“value=”
 *      7.没有成员定义的Annotation称为标记；包含成员变量的Annotation称为元数据Annotation。
 *
 *      如果注释有成员，在使用注解时，需要指明成员的值
 *      自定义注解必须配上注解的信息处理流程（使用反射）才有意义
 *      自定义注解通常都会指明两个元注解：Retention，Target
 *
 *  四、jdk提供的4种元注解
 *      元注解：对现有的注解进行解释说明的注解
 *      Retention:指定所修饰的Annotation的生命周期：SOURCE/CLASS(默认行为)/RUNTIME
 *                  只有声明为RUNTIME生命周期的注解，才能通过反射获取。
 *      Target:用于指定被修饰的Annotation作用于修饰哪些程序元素
 *      *******出现的频率较低*******
 *      Documented:表示所修饰的注释在被javadoc解析时，保留下来。
 *      Inherited:被它修饰的Annotation将具有继承性。
 *          我们在定义一个作用于类的注解时候，如果希望该注解也作用于其子类，那么可以用@Inherited 来进行修饰。（https://blog.csdn.net/sunnyzyq/article/details/119736442）
 *
 *  五、通过反射获取注解信息
 *
 *  六、jdk 8 中注解的新特性：可重复注解、类型注解
 *      可重复注解：
 *          1.在MyAnnotation上声明@Repeatable,成员值为RepeatableAnnotation.class
 *          2.MyAnnotation的（Target和Retention等原注解）和RepeatableAnnotation相同
 *      类型注解：
 *          ElementType.TYPE_PARAMETER 表示该注解能写在类型变量的声明语句中（如：泛型声明）。
 *          ElementType.TYPE_USE 表示该注解能写在使用类型的任何语句中。
 *
 *
 * @author bigDragon
 * @create 2020-10-27 14:34
 */
public class AnnotationMain {
    public static void main(String[] args){
        //自定义注解
        new MyAnnotationClass();
        //@Inherited 测试
        new InheritedTest();
        //可重读注解
        new RepeatableAnnotationClass();
    }
}
