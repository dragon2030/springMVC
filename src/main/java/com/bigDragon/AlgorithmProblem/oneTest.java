package com.bigDragon.AlgorithmProblem;

/**
 * @author: bigDragon
 * @create: 2023/4/3
 * @Description:
 */
public class oneTest {

    //算法题需要有个数据模型或者伪代码
    public static void main(String[] args) {
        oneTest main = new oneTest();
        int[] nums = {-1,0,3,5,9,12};
        int target = 13;
        int search = main.search(nums, target);
        System.out.println(search);
    }

    public int search(int[] nums, int target) {
        Integer targetIndex = this.getTargetIndex(nums, 0, nums.length - 1, target);
        return targetIndex;
    }
    //取中间值索引-长度奇数偶数
    //中间值比较目标值，如果不是就按照大小取前面或者后面的数组（以指针）
    //数组按照做第一步运算

    //通过递归不断移动中间值，定义下一个起始结束搜索索引
    public Integer getTargetIndex(int[] nums,int start,int end, int target){
        if(end==start){//就一个元素 返回,不是一个元素就接着判定
            if(nums[start]==target){
                return start;
            }else{
                return -1;
            }
        }
//        if(start<0 || end>nums.length-1){//发现已经移出数组的格子时候还没有找到目标值
//            return -1;
//        }
        if(end<=start){
            throw new RuntimeException("结束指针小于等于开始指针");
        }
        int length = end-start+1;
        //为奇数,中间数有值
        //偶数,中间数无值，取后一个值
        int middleIndex = start+length/2;

        int middleValue = nums[middleIndex];
        if(target == middleValue){//如果中间选取值就是目标值返回中间值指针
            return middleIndex;
        }else if(target < middleValue){
            end = middleIndex-1;
            if(end<0){return -1;}//如果移动出边界了，代表没有这个值
            return this.getTargetIndex(nums,start,end,target);
        }else{//target > middleValue
            start = middleIndex+1;
            if(start>nums.length-1){return -1;}//如果移动出边界了，代表没有这个值
            return this.getTargetIndex(nums,start,end,target);
        }
    }
}
