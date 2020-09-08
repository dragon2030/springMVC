package com.bigDragon.demo.util;

import java.security.MessageDigest;

public class MD5 {
	private static char[] hexs = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f'};

	public static String encode(String source) {
		try {
			MessageDigest e = MessageDigest.getInstance("MD5");
			byte[] sbs = source.getBytes("UTF8");
			e.update(sbs);
			byte[] rbs = e.digest();
			int j = rbs.length;
			char[] result = new char[j * 2];
			int k = 0;

			for (int i = 0; i < j; ++i) {
				byte b = rbs[i];
				result[k++] = hexs[b >>> 4 & 15];
				result[k++] = hexs[b & 15];
			}

			return new String(result);
		} catch (Exception arg8) {
			return null;
		}
	}
}

