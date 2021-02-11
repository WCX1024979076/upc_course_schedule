package com.example.upc.course;

import android.os.Build;
import androidx.annotation.RequiresApi;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class course_util {

    //用来判断学年
    public static String getYear() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        int year = Integer.parseInt(sdf.format(now));
        sdf = new SimpleDateFormat("MM");
        int month = Integer.parseInt(sdf.format(now));
        //八月前为上一学年
        if (month <= 8) {
            return (year - 1) + "-" + year;
        } else {
            return year + "-" + (year + 1);
        }
    }

    //用来判断学期
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getTerm() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH )+1;
        int term;
        if (month>=2 && month<=9)
            term = 2;
        else
            term = 1;
        return String.valueOf(term);
    }
}