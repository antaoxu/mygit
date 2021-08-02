package com.xat.interview.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTest {
    public static void main(String[] args) throws ParseException {
        dateTransfer();
    }

    public static String dateTransfer() throws ParseException {
        String dateTime = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("20210614");
        String date1 = format1.format(date);
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        dateTime = date1 + " " + time;
        System.out.println(dateTime.toString());
        return dateTime;
    }
}
