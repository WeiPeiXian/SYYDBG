package com.example.weipeixian.MYYDBG.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String formatDataTest(Date date) {
        StringBuilder sb = new StringBuilder();
        sb.append("yyyy年MM月dd日 HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat(sb.toString());
        String dateString = sdf.format(date);
        return dateString;
    }
}
