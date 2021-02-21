package com.xuan.dtrun.utils;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


public class DateUtils {

    public static String getDate() {
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.CHINA);
        return dateFormat.format(new Date());
    }


    public static String getDate(long time) {
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.CHINA);
        return dateFormat.format(new Date(time));
    }
}
