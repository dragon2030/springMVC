package com.bigDragon.javase.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * Collection接口
 *
 * 一、collection接口常用方法
 *      add(Object e);将元素e添加到集合中
 *      size():获取集合中元素的个数
 *      addAll(Collection c):将集合c添加到当前集合中
 *      isEmpty():判断当前集合是否为空
 *      clear():清空集合元素
 *      contains(Object obj):判断当期那集合中是否包含obj
 *      containsAll(Collection coll):判断形参coll中的所有元素是否都存在于当期集合当中
 *      remove(Object obj):从当前集合中移除obj元素
 *      removeAll(Collection coll1)：移除所有和形参coll中相同的元素，将查聚返回给当前集合
 *      retainAll(Collection coll1):获取当前集合和coll1的交集，并返回给当前集合
 *      equals(Object obj):判断两个集合是否相等（注：ArrayList要求顺序也一致）
 *      hasCode():返回当前对象的哈希值
 *      数组集合转换
 *          集合 --> 数组 ：Collection.toArray()
 *          数组 --> 集合 : Arrays.asList(Object[] obj)
 *      iterator()：返回Iterator接口的实例，用于遍历集合的元素。
 *
 * @author bigDragon
 * @create 2020-10-30 11:14
 */
public class CollectionTest {
    @Test
    public void method(){
        Collection collection = new ArrayList();

        //add(Object e);将元素e添加到集合中
        collection.add("AA");

        //size():获取集合中元素的个数
        System.out.println(collection.size());//1

        //addAll(Collection c):将集合c添加到当前集合中
        Collection collection2 = new ArrayList();
        collection2.add("BB");
        collection2.add("CC");
        collection.addAll(collection2);
        System.out.println(collection.size());//2
        print(collection);

        //isEmpty():判断当前集合是否为空
        System.out.println(collection.isEmpty());//false

        //clear():清空集合元素
        //collection.clear();
        //System.out.println(collection.size());//0
        //System.out.println(collection.isEmpty());//true

        //contains(Object obj):判断当期那集合中是否包含obj
        //collection.contains("AA");//true
        //collection.contains(new String("AA"));
        //collection.add(new Person("Mike",26));
        //System.out.println(collection.contains(new Person("Mike",26)));//true

        //containsAll(Collection coll):判断形参coll中的所有元素是否都存在于当期集合当中
        Collection coll = Arrays.asList("AA","BB");
        System.out.println(collection.containsAll(coll));//true

        //remove(Object obj):从当前集合中移除obj元素
        collection.remove("AA");//true

        //removeAll(Collection coll1)：移除所有和形参coll中相同的元素，将查聚返回给当前集合
        //collection.removeAll(ArrayTest.asList("AA","DD"));
        //print(collection);

        //retainAll(Collection coll1):获取当前集合和coll1的交集，并返回给当前集合
        //collection.retainAll(ArrayTest.asList("CC","II","OO"));
        //print(collection);

        //equals(Object obj):判断两个集合是否相等（注：ArrayList要求顺序也一致）
        System.out.println(collection.equals(Arrays.asList("CC")));

        //hasCode():返回当前对象的哈希值
        System.out.println(collection.hashCode());//2175

        //数组集合转换
        //集合 --> 数组 ：toArray()
        Object[] arr = collection.toArray();
        print2(arr);
        //数组 --> 集合 : ArrayTest.asList(Object[] obj)
        print(Arrays.asList(arr));
        System.out.println(Arrays.asList(arr));

        //iterator()：返回Iterator接口的实例，用于遍历集合的元素。
        collection.iterator();
    }

    /**
     * 打印遍历Collection
     * @param collection
     */
    public static void print(Collection collection){
        StringBuffer stringBuffer = new StringBuffer("collection[");
        //Iterator迭代方式，用于while循环
 /*     IteratorTest iterator = collection.iterator();
        while (iterator.hasNext()){
            stringBuffer.append((String) iterator.next());
            if (iterator.hasNext())
                stringBuffer.append(",");
        }*/
        //Iterator迭代方式，用于for循环
        for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
            stringBuffer.append(iterator.next());
            if (iterator.hasNext())
                stringBuffer.append(",");
        }
        stringBuffer.append("]");
        System.out.println(stringBuffer.toString());
    }

    /**
     * 打印遍历Object[]数组
     * @param array
     */
    public void print2(Object[] array){
        StringBuffer stringBuffer = new StringBuffer("array[");
        for(int i=0;i<array.length;i++){
            stringBuffer.append((String) array[i]);
            if (i != array.length-1)
                stringBuffer.append(",");
        }
        stringBuffer.append("]");
        System.out.println(stringBuffer.toString());
    }

}
