package org.spafka;

import java.sql.Timestamp;

public class Test {


    @org.junit.Test
    public void main1() {
        String time = new Timestamp(System.currentTimeMillis()).toString();
        System.out.println(time);
    }

}
