package org.spafka.hbase;

import java.util.Arrays;

public class HadminTest {

    public static void main(String[] args) {

        System.setProperty("hadoop.home.dir", "D:\\_20170526");
        Hadmin hadmin = new Hadmin();
//        hadmin.deleteTable("splitSql");
//        hadmin.createTable("splitSql", "col1,col2,col3".split(","),hadmin.getSplitKeys());

        //hadmin.insertData("splitSql","01|hdgjasgdj","col1","q1","v1");

        hadmin.queryAll("splitSql");
    }
}
