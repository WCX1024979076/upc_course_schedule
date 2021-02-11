package com.example.upc.course;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.upc.util.AsyncResponse;
import com.example.upc.util.cookie;

public class course_spider extends AsyncTask<Void, Void, String>
{
    private Map<Double,Boolean> color_use =null;
    private Map<String,Integer> bgMap=null;
    public AsyncResponse asyncResponse;
    public void setOnAsyncResponse(AsyncResponse asyncResponse) {
        this.asyncResponse = asyncResponse;
    }

    @Override
    protected String doInBackground(Void[] id) {
        try {
            bgMap=new HashMap<>(100);
            color_use=new HashMap<>(100);
            Log.e("课表爬虫","爬虫开始");
            course_db Course_DB = new course_db(MainActivity.context);
            Course_DB.course_clear();
            Map cookies = cookie.Readcookies();
            String Year = course_util.getYear();
            String Term = course_util.getTerm();
            for (int week = 1; week < 19; week++) {
                Response response = Jsoup.connect("https://app.upc.edu.cn/timetable/wap/default/get-datatmp")
                        .userAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.15)")
                        .method(Method.POST)
                        .data("year", Year)
                        .data("term", Term)
                        .data("week", String.valueOf(week))
                        .cookies(cookies)
                        .ignoreContentType(true)
                        .execute();
                Document document = response.parse();
                String course_content = document.body().text();
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(course_content.toString());
                com.alibaba.fastjson.JSONObject ans = (com.alibaba.fastjson.JSONObject) jsonObject.get("d");

                JSONArray weekday=(JSONArray) ans.get("weekdays");
                String[] week_array=new String[8];
                int week_sum=0;
                for (Iterator iterator = weekday.iterator(); iterator.hasNext();) {
                    String job = iterator.next().toString();
                    System.out.println(job);
                    week_array[week_sum++]=job;
                }

                String ans1 = ans.get("classes").toString();
                JSONArray jarr = JSONArray.parseArray(ans1);
                for (Iterator iterator = jarr.iterator(); iterator.hasNext(); ) {
                    JSONObject job = (JSONObject) iterator.next();
                    String lesson=job.get("lessons").toString();


                    int start_time = Integer.parseInt(lesson.substring(0, 2));
                    int end_time = Integer.parseInt(lesson.substring(lesson.length()-2, lesson.length()));

                    boolean flag=bgMap.containsKey(job.get("course_id"));
                    int color;
                    if(flag)
                        color=bgMap.get(job.get("course_id").toString());
                    else {
                        color = createRandomColor();
                        bgMap.put(job.get("course_id").toString(),color);
                    }
                    Course_DB.course_insert(job.get("course_id").toString(), week, Integer.parseInt(job.get("weekday").toString()), start_time, end_time, job.get("course_name").toString(), job.get("location").toString(),color,job.get("course_time").toString(),week_array[Integer.parseInt((String) job.get("weekday"))]);
                }
            }
            return "操作成功";
        } catch (Exception a) {
            return a.toString();
        }
    }
    @Override
    protected void onPostExecute(String msg) {
        super.onPostExecute(msg);
        Log.e("课表爬虫",msg);
        if (msg.equals("操作成功")) {
            asyncResponse.onDataReceivedSuccess(msg);//将结果传给回调接口中的函数
        }
        else {
            asyncResponse.onDataReceivedFailed(msg);
        }
    }
    private int createRandomColor() {
        int r,g,b;
        Random random = new Random();
        do{
            r= random.nextInt(256);
            g= random.nextInt(256);
            b= random.nextInt(256);
        }while ((r*0.299 + g*0.587 + b*0.114)>186&&!color_use.containsKey((r*0.299 + g*0.587 + b*0.114)));
        color_use.put((r*0.299 + g*0.587 + b*0.114),true);
        return Color.argb(255,r,g,b);
    }
}