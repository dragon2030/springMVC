package com.bigDragon.javase.collection;

import java.util.ArrayList;

/**
 * List接口概述
 *
 * 1.鉴于Java中数组用来存储数据的局限性，我们通常使用List替代数组
 * 2.List集合类中元素有序、且可重复，集合中的每个元素都有其对应的顺序索引。
 * 3.List容器中的元素都有对应一个整数数型的序号记载其在容器中的位置，可以根据需要存取容器的元素。
 * 4.JDK API中List接口的实现常用的有：ArrayList、LinkedList和Vector
 *
 * 结构体系：
 *      Collection接口：单列集合，用来存储一个一个的对象
 *          List接口：存储有序的、可重复的数据
 *              ArrayList：作为List接口的主要实现类；线程不安全的，效率高；底层使用Object[] elementData存储
 *              LinkedList:对于频繁的插入。删除操作，使用此类效率比ArrayList高；底层使用双向链表存储。
 *              Vector:作为List接口的古老实现类；线程安全，效率低；底层使用Object[] elementData存储
 *
 *
 *
 * @author bigDragon
 * @create 2020-11-03 16:05
 */
public class ListTest {
}
