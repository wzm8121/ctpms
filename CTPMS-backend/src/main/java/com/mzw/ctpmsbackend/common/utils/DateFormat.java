package com.mzw.ctpmsbackend.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {
    private DateFormat(){}
    // 获取当前时间
    public static String getNowDateFormat(){
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    public static String getNowDateFormatComment(){
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    public static String getNowCode(){
        SimpleDateFormat formatter= new SimpleDateFormat("yyyyMMdd");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

}
