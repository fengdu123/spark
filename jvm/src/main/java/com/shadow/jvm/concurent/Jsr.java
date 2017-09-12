package com.shadow.jvm.concurent;

import javax.annotation.Nullable;

public class Jsr {

    public static void main(String[] args) {

        String s =null;


        try {
            String t=s;
            System.out.println(t.length());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
