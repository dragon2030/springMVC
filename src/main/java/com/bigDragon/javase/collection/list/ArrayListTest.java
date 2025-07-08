package com.bigDragon.javase.collection.list;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ArrayList
 *
 * 一、ArrayList的源码分析
 *  1.1 jdk 7情况下
 *      ArrayList list = new ArrayList();//底层创建了长度是10的Object[]数组elementData
 *      list.add(123);//elementData[0] = new Integer(123);
 *      ...
 *      list.add(11);//如果此次的添加导致底层elementData数组容量不够，则扩容。
 *      默认情况下，扩容为原来容量的1.5倍，同时需要将原有数组中的数据复制到新的数组中。
 *      建议在开发中使用带参的构造器：ArrayList list= new ArrayList(int capacity)
 *
 *  1.2 jdk 8情况下
 *      ArrayList list = new ArrayList();//底层Object[] elementData初始化为{}，并没有创建长度为10的数组
 *      list.add(123);//第一次调用add()时，底层才创建了长度为10的数组，并将数据123添加到elementData中
 *      ...
 *      后续的添加和扩容操作和jdk 7无异
 *
 *  1.3 小结：jdk7中的ArrayList的对象创建类似于单例的饿汉模式，而jdk8中的ArrayList的对象的创建类似于单例的懒汉模式，延时了数组的创建，
 *  节省内存。
 *
 *  源码解析
 *  核心属性 protected transient int modCount = 0;
 *  简介：提供了fail-fast机制，这提供了快速失败（故障）行为，而不是在迭代期间面对并发修改时的不确定性行为。
 *  用途：用于记录列表结构被修改的次数。结构修改指的是那些改变列表大小或影响其遍历结果的修改，比如添加、删除元素，调整容量等。而仅仅是设置元素的值
 *      （比如set方法）不算结构修改，因为列表的大小没变，不影响遍历。
 *  具体原理：当使用迭代器遍历集合时，如果检测到在迭代过程中列表被修改（modCount变化），就会抛出ConcurrentModificationException，防止数据
 *      不一致。比如，在迭代时，另一个线程修改了列表，或者用户自己在遍历时调用了add或remove方法，这时候迭代器会发现modCount和预期的
 *      expectedModCount不一致，就会抛出异常。
 *  modCount如何工作：
 *      1、每次发生结构修改时，modCount 会递增。
 *      2、迭代器（如 Iterator、ListIterator）在初始化时会记录当前的 modCount 值（记为 expectedModCount）。
 *      3、在迭代过程中，每次操作（如 next()、remove()）都会检查 modCount == expectedModCount。如果不一致，抛出 
 *          ConcurrentModificationException。
 *      public Iterator<E> iterator() {return new Itr();}
 *      public E next() {checkForComodification();...}
 *      final void checkForComodification() {if (modCount != expectedModCount)
 *                 throw new ConcurrentModificationException();
 *      }
 *      
 *  ArrayList扩缩容
 *  核心代码
 *      //扩容
 *      int newCapacity = oldCapacity + (oldCapacity >> 1);
 *      //复制
 *      elementData = Arrays.copyOf(elementData, newCapacity);
 *      //存入元素
 *      elementData[size++] = e;
 *
 ArrayList底层结构及原理分析
 https://blog.csdn.net/Flying_Fish_roe/article/details/144110190?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522e4f68785391ee1a65ea8a28f2bf7b9b9%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=e4f68785391ee1a65ea8a28f2bf7b9b9&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~sobaiduend~default-1-144110190-null-null.142 
 *  
 * @author bigDragon
 * @create 2020-11-05 15:55
 */
public class ArrayListTest {
    public static void main(String[] args) {
        ArrayListTest test = new ArrayListTest();
        //java集合(Collection)中的一种错误机制:fail-fast 机制核心变量modCount
        //多线程修改时的报错场景
        test.modCountTest1();
        //这边用的是arrayList内部类Itr中的remove方法，会修改modCount也修改expectedModCount
        test.modCountTest2();
        //这边用的是arrayList中的remove方法，只会修改modCount不修改expectedModCount
        test.modCountTest3();
        //都是需要注意 使用时候最好用new AraayList（。。。）套一层的内部类
        test.test4();
        //安全删除的三种方式 非安全 的两种1、for(xxx : yyy) 2、for的下标正序遍历 安全1、for的下标倒序遍历2、list.stream().filter()3、迭代器 中的remove方法
        test.safeDelete();
        //去重 1 set 2 stream distinct
        test.removeDuplication();
        //排序
        test.sort();
    }
    public void method(){
        List list = new ArrayList();
    }
    
    public void modCountTest1(){
        List<String> strList = new ArrayList<>();
        strList.add("a");
        strList.add("b");
        strList.add("c");
        Iterator<String> iterator = strList.iterator();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> strList.add("d")).start();
        while (iterator.hasNext()) {
            String str = iterator.next();
            System.out.println(str);
        }
    }
    
    public void modCountTest2(){
        List<String> strList = new ArrayList<>();
        strList.add("AA");
        strList.add("aa");
        strList.add("aa");
        strList.add("CC");
        Iterator<String> iterator = strList.iterator();
        while (iterator.hasNext()) {
            if ("aa".equals(iterator.next())) {
                iterator.remove();
            }
        }
        strList.remove(1);
        System.out.println(strList);
    }
    
    public void modCountTest3(){
        List<String> strList = new ArrayList<>();

        strList.add("AA");
        strList.add("aa");
        strList.add("BB");
        strList.add("CC");
        for (String str : strList) {
            if ("aa".equals(str)) {
                strList.remove(str);
            }
        }
        System.out.println(strList);
        //ArrayList内部类型 注意会和原本list复用核心存储数组数据
        //private class SubList extends AbstractList<E> implements RandomAccess {
        strList.subList(1,2);
//        Arrays的内部类 
//        private static class ArrayList<E> extends AbstractList<E>
//                implements RandomAccess, java.io.Serializable
        Arrays.asList(1,2);
    }
    
    /**
     * 都是需要注意 使用时候最好用new AraayList（。。。）套一层的内部类
     */
    public void test4(){
        List<String> strList = new ArrayList<>();
    
        strList.add("AA");
        //ArrayList内部类型 注意会和原本list复用核心存储数组数据
        //private class SubList extends AbstractList<E> implements RandomAccess {
        strList.subList(0,1);
//        Arrays的内部类 
//        private static class ArrayList<E> extends AbstractList<E>
//                implements RandomAccess, java.io.Serializable
        Arrays.asList(1,2);
        
    }
    
    //安全删除的三种方式 非安全 的两种1、for(xxx : yyy) 2、for的下标正序遍历 安全1、for的下标倒序遍历2、list.stream().filter()3、迭代器 中的remove方法
    @Test
    public void safeDelete(){
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("b");
        list.add("c");
        list.add("d");
        
        //非安全 的两种
        //非安全--for(xxx : yyy)遍历
        for(String s:list){
            if(Objects.equals(s,"b")){
                list.remove(s);
            }
        }
        /**
         java.util.ConcurrentModificationException
         原因：调用的是ArrayList的remove方法，会改变modCount，迭代器遍历元素时候发现modCount和预期对不上直接报错
         同时是for增加
         */
        //非安全--for的下标正序遍历
        for(int i=0;i<list.size();i++){
            if(Objects.equals(list.get(i),"b")){
                list.remove(i);
            }
        }
        /**
         结果（有的没有删除掉） [a, b, c, d] 
         原因描述：遍历到删除元素时会System.arraycopy把后面元素整体前移，而指针已经到下一个为止，所以去除后的下一个元素没法判断到
         i=0 [a, b, b, c, d]
         i=1 [a, b, c, d]
         i=2 [a, b, c, d]
         */
        
        //正确的三种方法
        //正确的方法--for的下标倒序遍历
        for(int i=list.size()-1;i>=0;i--){
            if(Objects.equals(list.get(i),"b")){
                list.remove(i);
            }
        }
        //正确的方法--list.stream().filter().collect()
        list = list.stream().filter(i -> !Objects.equals(i, "b")).collect(Collectors.toList());
        //iterator迭代器 中的remove方法
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String s = it.next();
            if ("b".equals(s)) {
                it.remove();
            }
        }
        list.removeIf(item -> "b".equals(item));//iterator迭代器 中的remove方法 简化后
        System.out.println(list);
    }
    
    @Test
    public void removeDuplication(){
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("b");
        list.add("c");
        list.add("d");
    
//        list = list.stream().distinct().collect(Collectors.toList());
        list = new ArrayList<>(new HashSet<String>(list));
        System.out.println(list);
    }
    
    @Test
    public void sort(){
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("b");
        list.add("c");
        list.add("d");
        //JDK8的stream
//        list = list.stream().sorted((v1,v2)->v1.compareToIgnoreCase(v2)).collect(Collectors.toList());
        //Collections.sort
//        Collections.sort(list,(v1,v2)->v1.compareToIgnoreCase(v2));
        list.sort((v1,v2)->v1.compareToIgnoreCase(v2));
        System.out.println(list);
    }
}
