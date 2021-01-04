package com.bigDragon.practice;

/**
 * 在控制台打印数字
 * 要求n*n,回字型连续数字，从1开始
 * @author bigDragon
 * @create 2020-10-21 16:06
 */
public class test20201209 {
    public static void main(String[] args){
        test20201209 main = new test20201209();
        int num = 10;
        main.test(num);

    }

    public void test(int num){
        //新建
        int [][] array = new int[num][num];
        int count = 0;//赋值的数字
        int row = 0;//指针行计数
        int rank = 0;//指针列计数
        int flag = 0;//赋值完成标志
        //赋值
        lable:
        while (true){
            //右
            for(int i = rank;i < array[row].length;i++){

                if(array[row][rank] != 0){//当前步判断，有值时跳出外循环
                    break lable;
                }
                array[row][rank] = ++count;//赋值
                if(rank+1 == num || array[row][rank+1] != 0){//赋值完成推动指针
                    row++;
                    break ;
                }else {
                    rank++;
                }
                flag = 1;
            }
            //下
            for(int i = row;i < array.length;i++){
                if(array[row][rank] != 0){
                    break lable;
                }
                array[row][rank] = ++count;
                if(row+1 == num || array[row+1][rank] != 0){//赋值完成推动指针
                    rank--;
                    break ;
                }else {
                    row++;
                }
                flag = 1;
            }
            //左
            for(int i = rank;i >= 0;i--){
                if(array[row][rank] != 0){
                    break lable;
                }
                array[row][rank] = ++count;
                if(rank == 0 || array[row][rank-1] != 0){//赋值完成推动指针
                    row--;
                    break ;
                }else {
                    rank--;
                }
                flag = 1;
            }
            //上
            for(int i = row;i >= 0;i++){
                if(array[row][rank] != 0){
                    break lable;
                }
                array[row][rank] = ++count;
                if(row == 0 || array[row-1][rank] != 0){//赋值完成推动指针
                    rank++;
                    break ;
                }else {
                    row--;
                }
                flag = 1;
            }
        }
        //输出
        for(int i = 0;i < array.length;i++){
            for(int j = 0;j < array[i].length;j++){
                System.out.print(array[i][j]+"\t");
            }
            System.out.println("");
        }
    }
}
