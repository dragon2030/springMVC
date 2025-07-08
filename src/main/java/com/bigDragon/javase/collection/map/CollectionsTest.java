package com.bigDragon.javase.collection.map;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Collections:操作Collection和 Map的工具类
 *
 * Collections概述：Collections中提供了一系列静态的方法对集合元素进行排序、查询和修改等操作，还提供了对集合对象设置不可变、对集合对象实现同步控制等方法。
 *
 * 常用方法：
 *      排序操作：
 *          reverse(List):反转List中元素的顺序
 *          shuffle(List):对List集合元素进行随机排序
 *          sort(List):根据元素的自然顺序对指定List集合元素按升序排序
 *          sort(List,Comparator):根据指定的Comparator产生的顺序对List集合进行排序
 *          swap(List,int,int):将指定List集合中的i处元素和j处元素进行交换
 *      查找、替换
 *          Object max(Collection):根据自然的元素顺序，返回给定计划中的最大元素
 *          Object max(Collection,Comparator):根据Comparator指定的顺序，返回给定集合中的最大元素
 *          Object min(Collection):根据自然的元素顺序，返回给定计划中的最小元素
 *          Object min(Collection,Comparator):根据Comparator指定的顺序，返回给定集合中的最小元素
 *          int frequency(Collection,Object):返回指定集合中指定元素的出现次数
 *          void copy(List dest,List src):将src中的内容复制到dest中
 *          boolean replaceAll(List list,Object oldVal,Object newVal):使用新值替换List对象的所有旧值
 *      同步控制
 *          Collections类中提供了多个synchronizedXxx()方法，该方法可使将指定集合包装成线程同步的集合，从而可以解决多线程并发访问集合时的线程安全问题。
 *          synchronizedCollection(Collection<T> c):Returns a synchronized (thread-safe) collection backed by the specified collection.
 *          synchronizedList(List<T> list):Returns a synchronized (thread-safe) list backed by the specified list.
 *          synchronizedMap(Map<K,V> m):Returns a synchronized (thread-safe) map backed by the specified map.
 *          synchronizedSet(Set<T> s):Returns a synchronized (thread-safe) set backed by the specified set.
 *
 *
 * @author bigDragon
 * @create 2020-11-21 20:52
 */
public class CollectionsTest {
    public static void main(String[] args){
        CollectionsTest collectionsTest = new CollectionsTest();
        //Collections中排序操作
        collectionsTest.test();
        //Collections中查找、替换操作
        collectionsTest.test2();
        //同步控制
        collectionsTest.synchronizedTest();
    }

    /**
     * Collections中排序操作
     */
    @Test
    public void test(){
        List list = new ArrayList();
        list.add(123);
        list.add(678);
        list.add(395);
        list.add(762);
        //reverse(List):反转List中元素的顺序
        Collections.reverse(list);
        System.out.println(list);//[762, 395, 678, 123]
        //shuffle(List):对List集合元素进行随机排序
        Collections.shuffle(list);
        System.out.println(list);//[762, 395, 123, 678]
        //sort(List):根据元素的自然顺序对指定List集合元素按升序排序
        Collections.sort(list);
        System.out.println(list);//[123, 395, 678, 762]
        //sort(List,Comparator):根据指定的Comparator产生的顺序对List集合进行排序
        Collections.sort(list, new Comparator(){
            //从大到小排序
            @Override
            public int compare(Object o1, Object o2) {
                if(o1 instanceof Integer && o2 instanceof  Integer){
                    int i1 = (int)o1;
                    int i2 = (int)o2;
                    return -Integer.compare(i1,i2);
                }else {
                    throw new RuntimeException("输入的数据类型不匹配");
                }
            }
        });
        System.out.println(list);//[762, 678, 395, 123]
        //swap(List,int,int):将指定List集合中的i处元素和j处元素进行交换
        Collections.swap(list,0,1);
        System.out.println(list);//[678, 762, 395, 123]
    }

    /**
     * Collections中查找、替换操作
     */
    @Test
    public void test2(){
        List list = new ArrayList();
        list.add(123);
        list.add(678);
        list.add(395);
        list.add(395);
        list.add(76);
        //Object max(Collection):根据自然的元素顺序，返回给定计划中的最大元素
        Object object = Collections.max(list);
        System.out.println(Integer.valueOf(object.toString()));//678
        //Object max(Collection,Comparator):根据Comparator指定的顺序，返回给定集合中的最大元素
        Object object2 = Collections.max(list, new Comparator<Object>() {//按照字符串大小比较
            @Override
            public int compare(Object o1, Object o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
        System.out.println(Integer.valueOf(object2.toString()));//76
        //int frequency(Collection,Object):返回指定集合中指定元素的出现次数
        Object object3 = Collections.frequency(list,395);//2
        System.out.println(Integer.valueOf(object3.toString()));
        // void copy(List dest,List src):将src中的内容复制到dest中
/*      错误方法 报错：java.lang.IndexOutOfBoundsException: Source does not fit in dest
        List list2 = new ArrayList(list.size());*/
        //正确方法
        List list2 = Arrays.asList(new Object[list.size()]);//值全为null
        Collections.copy(list2,list);
        System.out.println(list2);
        //boolean replaceAll(List list,Object oldVal,Object newVal):使用新值替换List对象的所有旧值
        Collections.replaceAll(list,123,12);
        System.out.println(list);
    }

    /**
     * 同步控制
     */
    @Test
    public void synchronizedTest(){
        List list = new ArrayList();
        list.add(123);
        list.add(678);
        list.add(395);
        list.add(395);
        list.add(76);
        List list1=Collections.synchronizedList(list);
        list1.add(new Object());
    }
}
