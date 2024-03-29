package com.example.upc.course;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class course_util {

    //用来判断学年
    public static String getYear() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        int year = Integer.parseInt(sdf.format(now));
        sdf = new SimpleDateFormat("MM");
        int month = Integer.parseInt(sdf.format(now));
        //八月前为上一学年
        Log.e("年份判断",String.valueOf(year)+"-"+String.valueOf(month));
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
        if (month>=2 && month<=8)
            term = 2;
        else
            term = 1;
        Log.e("学期判断",String.valueOf(term));
        return String.valueOf(term);
    }

    //用来判断星期
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long getWeek(String[] week_data)
    {
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            long start = sf.parse(week_data[2]).getTime();// 日期转换为时间戳
            long now=System.currentTimeMillis();
            long start_day=(Integer.parseInt(week_data[0])*7+Integer.parseInt(week_data[1]))*24*60*60*1000;
            long diff=now-start+start_day; ///获取差值
            if(diff<0)
                diff=0;
            long week=diff/(7*24*60*60*1000);
            if(week==0)
                week=1;
            Log.e("周次判断","数据库时间"+String.valueOf(start)+"现在的时间"+String.valueOf(now)+"差值"+String.valueOf(diff));
            return week;
        }
        catch (ParseException e) {
            e.printStackTrace();
            Log.e("周次判断",e.toString());
            return 1;
        }
    }

    public static List<Date> GetWeekDays() {
        int page=MainActivity.page;
        int page_start=MainActivity.page_start;
        Date mdate = new Date();
        int b = mdate.getDay();
        Date fdate;
        List<Date> list = new ArrayList<Date>();
        Long Hour = Long.valueOf(24) * Long.valueOf(3600000);
        Long fTime = mdate.getTime() - Long.valueOf(b) * Hour + Long.valueOf(page-page_start) * Long.valueOf(7) * Hour;
        for (int a = 0; a <= 6; a++) {
            fdate = new Date();
            fdate.setTime(fTime + (Long.valueOf(a) * Hour));
            list.add(a, fdate);
        }
        return list;
    }
}