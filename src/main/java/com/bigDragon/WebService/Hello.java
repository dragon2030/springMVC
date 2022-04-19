package com.bigDragon.WebService;

/**
 * @author bigDragon
 * @create 2021-01-18 9:56
 */
public class Hello {
    public static void main(String[] args) {
        byte[] a1s = new Hello().getByte("a1");
        for (int i = 0; i < a1s.length; i++) {
            for (int i1 = 0; i1 < 8; i1++) {
                System.out.print((a1s[i] >> (7 - i1)) & 1);
            }
        }
    }

    public byte[] getByte(String hex) {
        byte[] bytes = new byte[hex.length() >>1];
        for (int i = 0; i < bytes.length; i++) {
            char c1 = hex.charAt(i * 2);
            char c2 = hex.charAt(i * 2+1);
            bytes[i] = (byte) (bytes[i] | (getBinary(c1) << 4));
            bytes[i] = (byte) (bytes[i] | getBinary(c2));
        }
        return bytes;
    }

    private byte getBinary(char c1) {
        if (isNumber(c1)) {
            return (byte) (c1-'0');
        }else {
            return (byte) (c1-'a'+10);
        }
    }

    private boolean isNumber(char c) {
        return (c & 0x00f0) == 0X0030;
    }
}