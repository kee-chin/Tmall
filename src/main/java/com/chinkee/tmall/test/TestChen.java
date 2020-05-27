package com.chinkee.tmall.test;

public class TestChen {
    public static void main(String args[]){
        int i = 5;
        int j = 8;
        int z = i - j;
        System.out.println(z);// int包括正负整数

        String a = new String();
        String b = "";

        if(a == b)
            System.out.println("a地址就等于b");
        if(a.equals(b))
            System.out.println("a的值等于b");

        System.out.println(!a.isEmpty());
    }
}
