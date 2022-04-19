package com.bigDragon.javase.CompareAPI;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author bigDragon
 * @create 2021-03-04 20:50
 */
public class ComparatorTest {
    public static void main(String[] args) {
        String[] array=new String[]{"aa","cc","bb"};
        Arrays.sort(array, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        System.out.println(Arrays.toString(array));//[aa, bb, cc]
    }
}
