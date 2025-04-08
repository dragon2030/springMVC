package com.bigDragon.javase.collection.map;

import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Map
 *
 * 一、Map的实现类的结构：
 * |---Map:双列数据，存储key-value对的数据
 *      |---HashMap：作为Map的主要实现类；线程不安全，效率高；存储null的key和value
 *          |---LinkedHashMap：保证在遍历map元素时，可以按照添加的顺序实现遍历。
 *              原因：在原有的HashMap底层结构基础上，添加了一堆指针，指向前一个和后一个元素。
 *              对于频繁的遍历操作，此类执行效率高于HashMap
 *      |---TreeMap：保证按照添加的key-value对数据进行排序，实现排序遍历。此时考虑key的自然排序和定制排序
 *                  底层使用红黑树
 *      |---Hashtable：作为古老的实现类；线程安全的效率低；不能存储null的key和value
 *          |---Properties:常用来处理配置文件。key和value都是String类型
 *
 *      HashMap的底层：  数组+链表 （jdk7及之前）
 *                      数组+链表+红黑数（jdk 8）
 *      CurrentHashMap:HashMap对多线程的支持
 *  二、Map结构的理解：
 *      Map中的key：无序的、不可重复的，使用Set存储所有的key
 *          --->key所在的类要重写equals()和hashCode()（HashMap为例）
 *          --->key所在的类要重写compareTo()和compare()（TreeMap为例）
 *      Map中的value：无序的、可重复的，使用Collection存储所有的value
 *          --->value所在类要重写equals()
 *      一个键值对：key-value构成了一个Entry对象
 *      Map中的entry：无序的、不可重复的，使用Set存储所有的entry
 *  三、HashMap的底层实现原理？以jdk7为例说明：
 *      HashMap map = new HashMap():
 *      在实例化以后，底层创建了长度为16的一位数组Entry[] table。
 *      map.put(kay1,value1):
 *      (hash(key))首先，调用key1所在类的hashCode()计算key1哈希值，此哈希值经过某种算法计算以后，得到Entry数组中的存放位置。
 *      如果此位置上的数据为空，此key1-value1添加成功。   ---情况1
 *      如果此位置上的数据不为空，（意味着此位置上存在一个或多少数据（以链表形式存在）），比较key1和已经存在的一个或多个数据的哈希值：
 *          如果key1的哈希值与已经存在的书的哈希值都不相同，此时key1-value1添加成功 ---情况2
 *          如果key1的哈希值和已经存在的某一个数据（key2-value2）的哈希值相同，继续比较：调用key1所在类的equals（key2）方法比较：
 *              如果equals()返回false：此时key1-value1添加成功 ---情况3
 *              如果equals()返回true：此时value1替换value2
 *
 *      补充：关于情况2和情况3：此时key1-value1和原来的数据以链表的方式存储。
 *      在不断添加的过程中，会这几到扩容问题，当超出临界值（且要存放的位置非空），扩容
 *      默认的扩容方式：扩容为原来容量的2倍，并将原有的数据复制过来
 *
 *      jdk8 相较于jdk7在底层实现方面的不同：
 *      1、new HashMap()：底层没有创建一个长度为16的数组
 *      2、jdk8底层的数组是：Node[]，而非Entry[]
 *      3、首次调用put()方法时，底层创建长度为16的数组
 *      4、jdk7底层结构只有：数组+链表。jdk8的底层结构：数组+链表+红黑树。
 *         当数组的魔一个索引位置上的元素以链表形式存在的数据个数>8时且当前数组的长度>64时，此时此索引位置上的所有数据改为使用红黑数存储
 *
 *      DEFAULT_INITIAL_CAPACITY : HashMap的默认容量---16
 *      DEFAULT_LOAD_FACTOR : HashMap的默认加载因子---0.75
 *      threshold : 扩容的临界值，=容量*填充因子---16 * 0.75 => 12
 *      TREEIFY_THRESHOLD : Bucket中链表长度大于该默认值，转化为红黑树---8
 *      MIN_TREEIFY_CAPACITY : 桶中的Node被树化时最小的hash表容量---64
 *
 *  四、LinkedHashMap的底层原理
 *      源码中：
         static class Entry<K,V> extends HashMap.Node<K,V> {
             Entry<K,V> before, after; ----能够记录添加的元素的先后顺序
             Entry(int hash, K key, V value, Node<K,V> next) {
                super(hash, key, value, next);
             }
         }
 *  五、Map中定义的方法：
 *  添加、删除、修改操作：
 *  Object put(Object key,Object value):将key-value添加到（或修改）当前map对象中
 *  void putAll(Map m)：将m中的所有key-value对存放到当前map中
 *  Object remove(Object key)：移除指定key的key-value对，并返回value
 *  void clear():清空当前map中所有数据
 *  元素查询的操作：
 *  Object get(Object key)：获取指定key对应的value
 *  boolean containsKey(Object key)：是否包含指定的key
 *  boolean containsValue(Object value):是否包含指定的value
 *  int size():返回map中key-value对的个数
 *  boolean isEmpty():判断当前map是否为空
 *  boolean equals(Object obj):判断当前map和参数对象obj是否相等
 *  元视图操作的方法：
 *  Set keySet():返回所有key构成的Set集合
 *  Collection values():返回所有value构成的Collection集合
 *  Set entrySet():返回所有key-value对构成的Set集合
 *
 * @author bigDragon
 * @create 2020-11-16 16:12
 */
public class MapMain {
    public static void main(String[] args){
        MapMain mapMain = new MapMain();
        //对比比较HashMap和LinkedHashMap区别
        mapMain.main();
        mapMain.main2();
        //Map中的添加、删除、修改操作
        mapMain.test1();
        //Map中元素查询的操作
        mapMain.test2();
        //Map中元视图操作的方法
        mapMain.test3();
        //TreeMap
        new TreeMapTest();
    }
    
    /**
     put
     
     */
    @Test
    public void main(){
        Map map = new HashMap();
        map.put(123,"AA");
        map.put(234,"BB");
        map.put(345,"CC");
        System.out.println(map);
    }

    @Test
    public void main2(){
        Map<Integer,String> map = new LinkedHashMap();
        map.put(123,"AA");
        map.put(234,"BB");
        map.put(345,"CC");
//        System.out.println(map);
        Set<Map.Entry<Integer, String>> entries = map.entrySet();
        for(Map.Entry<Integer,String> entry:entries){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }

    /**
     *  Object put(Object key,Object value):将key-value添加到（或修改）当前map对象中
     *  void putAll(Map m)：将m中的所有key-value对存放到当前map中
     *  Object remove(Object key)：移除指定key的key-value对，并返回value
     *  void clear():清空当前map中所有数据
     */
    @Test
    public void test1(){
        Map map = new HashMap();
        //Object put(Object key,Object value):将key-value添加到（或修改）当前map对象中
        map.put(123,"AA");
        System.out.println(map);

        //void putAll(Map m)：将m中的所有key-value对存放到当前map中
        Map map2 = new HashMap();
        map2.put(234,"BB");
        map2.put(345,"CC");
        map.putAll(map2);
        System.out.println(map);

        //Object remove(Object key)：移除指定key的key-value对，并返回value
        map.remove(234);
        System.out.println(map);

        //void clear():清空当前map中所有数据
        map.clear();
        System.out.println(map);
    }

    /**
     *  元素查询的操作：
     *  Object get(Object key)：获取指定key对应的value
     *  boolean containsKey(Object key)：是否包含指定的key
     *  boolean containsValue(Object value):是否包含指定的value
     *  int size():返回map中key-value对的个数
     *  boolean isEmpty():判断当前map是否为空
     *  boolean equals(Object obj):判断当前map和参数对象obj是否相等
     *  getOrDefault：Returns the value to which the specified key is mapped, or defaultValue if this map contains no mapping for the key.
     */
    @Test
    public void test2(){
        Map<Integer,String> map = new HashMap<Integer,String>();
        map.put(123,"AA");
        map.put(234,"BB");
        map.put(345,"CC");
//        //Object get(Object key)：获取指定key对应的value
//        System.out.println(map.get(123));//AA
//
//        //boolean containsKey(Object key)：是否包含指定的key
//        System.out.println(map.containsKey(123));//true
//
//        //boolean containsValue(Object value):是否包含指定的value
//        System.out.println(map.containsValue("AA"));//true
//
//        //int size():返回map中key-value对的个数
//        System.out.println(map.size());//3
//
//        //boolean isEmpty():判断当前map是否为空
//        System.out.println(map.isEmpty());//false
//
//        //boolean equals(Object obj):判断当前map和参数对象obj是否相等
//        Map map2 = new HashMap();
//        map2.put(123,"AA");
//        map2.put(234,"BB");
//        map2.put(345,"CC");
//        System.out.println(map.equals(map2));//false

        //Returns the value to which the specified key is mapped, or defaultValue if this map contains no mapping for the key.
        String st = map.getOrDefault(123, new String("abc"));
        System.out.println(st);
        String st2 = map.getOrDefault(456, new String("abc"));
        System.out.println(st2);
    }

    /**
     *  元视图操作的方法：
     *  Set keySet():返回所有key构成的Set集合
     *  Collection values():返回所有value构成的Collection集合
     *  Set entrySet():返回所有key-value对构成的Set集合
     */
    @Test
    public void test3(){
        Map map = new HashMap();
        map.put(123,"AA");
        map.put(234,"BB");
        map.put(345,"CC");
        //Set keySet():返回所有key构成的Set集合
        Set set = map.keySet();
        System.out.println(set);

        //Collection values():返回所有value构成的Collection集合
        Collection collection = map.values();
        System.out.println(collection);

        //遍历所有的key-value
        //方式一：entrySet()
        //Set entrySet():返回所有key-value对构成的Set集合
        Set entrySet=map.entrySet();
        Iterator iterator = entrySet.iterator();
        while (iterator.hasNext()){
            Object object = iterator.next();
           Map.Entry entry = (Map.Entry) object;
           System.out.println(entry.getKey()+":"+entry.getValue());
        }
        //方式二：
        Set set2 = map.keySet();
        Iterator iterator2 = set2.iterator();
        while (iterator2.hasNext()){
            Object key = iterator2.next();
            Object value = map.get(key);
            System.out.println(key+"--->"+value);
        }
    }
    
    public void mutilSafe(){
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        concurrentHashMap.put("1","1");
    
    }
}
