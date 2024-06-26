package com.bigDragon.javase.baseTest;

import com.bigDragon.javase.faseToObject.interfasce.A;
import com.bigDragon.model.User;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author bigDragon
 * @create 2021-03-22 10:22
 */
public class ArrayTest {
    @Test
    public void test1(){
        User user = new User(1,"Mike","22","","");
        User[] users=new User[]{user};
        System.out.println(java.util.Arrays.toString(users));//[User864237698{userId=1, age='Mike', name='22', peopleDes='', sexId=''}]
    }
    //一维数组
    @Test
    public void test2(){
/*        int[][] ArrayTest = new int[5][5]{};
        ArrayTest[1][1]=1;*/
        int num;//声明
        num = 10;//初始化
        int id = 1001;//声明+初始化

        //静态初始化：数组的初始化和数组元素的赋值操作同时进行
        int[] ints = new int[]{1,2,3,4,5};
        //动态初始化：数组的初始化和数组元素的赋值操作分开进行
        int[] ints2 = new int[5];
        //也是正确的写法
        int[] array = {1,2,3,4,5};//类型推断
        //如何调用数组的指定位置的元素：通过角标的方式调用
        //数组的角标（或索引）从0开始的，到数组的长度-1结束
/*        ints2[0] = 0;
        ints2[1] = 1;
        ints2[2] = 2;
        ints2[3] = 3;
        ints2[4] = 4;
        System.out.println(Arrays.toString(ints2));//[0, 1, 2, 3, 4]
        int length = ints2.length;
        System.out.println(length);//5
        int[] ints3 = new int[3];
        System.out.println(Arrays.toString(ints3));//[0, 0, 0]
        double[] doubles = new double[3];
        System.out.println(Arrays.toString(doubles));//[0.0, 0.0, 0.0]
        char[] chars = new char[3];
        System.out.println(Arrays.toString(chars));//[ ,  ,  ]
        for (char c : chars){
            System.out.println((byte)c);
        }*/
        boolean[] booleans = new boolean[3];
        System.out.println(Arrays.toString(booleans));//[false, false, false]
        String[] strings = new String[3];
        System.out.println(Arrays.toString(strings));//[null, null, null]
    }
    @Test
    public void test3() {
        //一维数组的声明和初始化（用于对比）
        int[] arr = new int[]{1, 2, 3};
        //二维数组静态初始化
        int[][] arr1 = new int[][]{{1, 2, 3}, {4, 5, 6}};
        //二维数组动态初始化1
        int[][] arr2 = new int[2][3];
        //初始化一个长度为二的一维数组，每一个元素都是长度为三的一维数组
        //二维数组动态初始化2
        int[][] arr3 = new int[2][];
        arr3[0]=new int[]{1,2};
        System.out.println(arr3[0][1]);
        //初始化一个长度为二的一维数组,每一个元素未被初始化

        //也是正确的写法
        int[] arr4[] = new int[][]{{1, 2, 3}, {4, 5, 6}};
        int[] arr5[] = {{1, 2, 3}, {4, 5, 6}};//类型推断
        //二维数组调用指定位置的元素
        System.out.println(arr5[0][2]);//3
        int[][] arr6 = new int[3][];

        int[][] arr7 = {{1, 2, 3},{4}};
        System.out.println(arr7.length);//2
        System.out.println(arr7[0].length);//3
        System.out.println(arr7[1].length);//1

        int[][] arr8 = {{1, 2, 3},{4,5}};
        for(int i=0;i<arr8.length;i++){
            for(int j=0;j<arr8[i].length;j++){
                System.out.print(arr8[i][j]+"\t");
            }
            System.out.println();
        }
    }
    @Test
    public void test4(){
        int[][] arr1= {{1, 2, 3}, {4, 5, 6}};
        System.out.println(arr1[0]);//[I@33833882——地址值
        System.out.println(arr1[1][0]);//1
        System.out.println(arr1);//[[I@200a570f
        int[][] arr2 = new int[2][];
        System.out.println(arr2[1]);//null
        System.out.println(new SimpleDateFormat());//java.text.SimpleDateFormat@b5341f2a
        new Object();
    }
    //二分查找
    @Test
    public void test5(){
        //二分查找前提：所要查找的数组必须有序
        int[] arr1 = {1,2,3,4,5,6,7,8,9,10};//所有搜索范围
        int dest = 10;//需被查找数

        int num = 0;//查询次数
        int start = 0;
        int end = arr1.length-1;
        int index;
        index = (end+start)/2;//二分中间的指针
        while(dest != arr1[index]){
            if(dest < arr1[index]){
                end = index-1;
            }else if(dest > arr1[index]){
                start = index+1;
            }
            index = (end+start)/2;//二分中间的指针
            num++;
        }
        System.out.println("查找到第"+(index+1)+"个数字,已经查询遍数："+num);

    }
    //Arrays工具类的调用
    @Test
    public void test6(){
        //boolean equals(int[] a, int[] a2)
        //判断两个数组是否相等
        int[] arr1 = new int[]{1,2,3,4};
        int[] arr2 = new int[]{1,2,3,4};
        boolean equals = Arrays.equals(arr1, arr2);
        System.out.println(equals);//true

        //String toString(int[] a)
        //输出数组的信息
        int[] arr3 = new int[]{1,2,3,4};
        String s = Arrays.toString(arr3);
        System.out.println(s);//[1, 2, 3, 4]

        //void fill(int[] a, int val)
        //将指定的值填充到数组中
        int[] arr4 = new int[]{1,2,3,4};
        Arrays.fill(arr4,10);
        System.out.println(Arrays.toString(arr4));//[10, 10, 10, 10]

        //void sort(int[] a)
        //对数组进行排序（底层用的快速排序）
        int[] arr5 = new int[]{4,3,2,1};
        Arrays.sort(arr5);
        System.out.println(Arrays.toString(arr5));//[1, 2, 3, 4]

        //int binarySearch(int[] a, int key)
        //对排序后的数组进行二分法检索指定的值
        int[] arr6 = new int[]{1,2,3,4};
        int i = Arrays.binarySearch(arr5, 4);
        System.out.println(i);//3
    }
    //数组异常
    @Test
    public void ArrayException(){
        //java.lang.ArrayIndexOutOfBoundsException:数组角标越界
        int[] arr1 = {1,2,3};
        System.out.println(arr1[3]);//java.lang.ArrayIndexOutOfBoundsException: 3
        //java.lang.NullPointerException:空指针异常
        String st = null;
        System.out.println(st.length());//java.lang.NullPointerException
    }


    /**
     * 数组和Collection相互转换
     */
    @Test
    public void test10(){
        String[] strArray = {"1","2","3"};
        List<String> list = new ArrayList<>(Arrays.asList(strArray));
        System.out.println(list);
        String[] strings = list.toArray(new String[list.size()]);
        System.out.println(strings);
        for(int i=0;i<strings.length;i++){
            System.out.println(strings[i]);
        }
    }
}
