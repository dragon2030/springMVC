package com.bigDragon.test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws UnsupportedEncodingException {
		List<String> list = new LinkedList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		list.add("d");
		list.add("e");
		System.out.println(list);
		list.add(5,"z");
		System.out.println(list);
		list.remove(list.size()-1);
		System.out.println(list);

    }
}
