package com.bigDragon.javase.collection;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * LinkedList
 *
 * 一、LinkedList的源码分析
 *  LinkedList list = new LinkedList();内部声明了Node类型的first和last属性，默认值为null
 *  list.add(123);//将123封装到Node中，创建了Node对象。
 *
 *  其中，Node定义为：提现了LinkedList的双向链表的说法
 *      private static class Node<E> {
 *         E item;
 *         Node<E> next;
 *         Node<E> prev;
 *
 *         Node(Node<E> prev, E element, Node<E> next) {
 *             this.item = element;
 *             this.next = next;
 *             this.prev = prev;
 *         }
 *     }
 *
 *
 * @author bigDragon
 * @create 2020-11-05 16:16
 */
public class LinkedListTest {
    @Test
    public void test(){
        List list = new LinkedList();
    }
}
