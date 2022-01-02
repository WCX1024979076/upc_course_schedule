package com.example.upc.course;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.upc.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.example.upc.login.login_db;
import com.example.upc.util.AsyncResponse;
import com.example.upc.util.cookie;
import static java.lang.Integer.max;
import static java.lang.Math.abs;
import static java.lang.Math.min;


/**
 * @author Meteor
 */
public class MainActivity extends AppCompatActivity
{
    private TableView<CustomLesson> tableView;
    private course_db Course_DB=null;
    private login_db Login_DB=null;
    public static Context context=null;
    ProgressBar loadingProgressBar=null;
    ImageView next=null,back=null;
    TextView page_num=null;
    ImageView imageview=null;
    static int page=1;
    static int page_start=1;
    public static double base_distance=0,base_dp=0;
    public static int height_per_lesson= 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_course);
        context=this;
        Course_DB = new course_db(this);
        Login_DB= new login_db(this);
        height_per_lesson=(int) context.getResources().getDimension(R.dimen.height_per_lesson);
        loadingProgressBar = findViewById(R.id.loading_course);
        next = findViewById(R.id.next);
        back = findViewById(R.id.back);
        imageview=findViewById(R.id.setting);
        page_num= findViewById(R.id.page);
        page_num.setGravity(Gravity.CENTER_HORIZONTAL);

        if(cookie.path_cookie==null)
            cookie.path_cookie=this.getCacheDir()+"/cookie.txt";    ///获取缓存路径

        if(!Login_DB.login_check())
        {
            Toast.makeText(getApplicationContext(), "请先登录！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, com.example.upc.login.LoginActivity.class);
            startActivity(intent);
            finish();
            return ;
        }
        //Course_DB.course_upgrade(); /// 更新数据库

        cal_page_start();
        page=page_start;
        update_page();
        page_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page=page_start;
                update_page();
            }
        });

        next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                height_per_lesson=(int) context.getResources().getDimension(R.dimen.height_per_lesson);
                page++;
                page=min(page,18);
                update_page();
            }
        });
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                height_per_lesson=(int) context.getResources().getDimension(R.dimen.height_per_lesson);
                page--;
                page=max(page,1);
                update_page();
            }
        });
        imageview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.context, com.example.upc.setting.SettingActivity.class);
                startActivity(intent);
                finish();
                return ;
            }
        });
        if(!Course_DB.course_check())
        {
            Log.e("课表","开始");
            Toast.makeText(getApplicationContext(), "开始爬取课程信息", Toast.LENGTH_SHORT).show();
            loadingProgressBar.setVisibility(View.VISIBLE);
            course_spider Course_Spider = new course_spider();
            Course_Spider.execute();
            Course_Spider.setOnAsyncResponse(new AsyncResponse() {
                @Override
                public void onDataReceivedSuccess(String msg)
                {
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "获取信息成功", Toast.LENGTH_SHORT).show();
                    Log.e("课表爬虫", "爬取成功");
                    cal_page_start();
                    page=page_start;
                    update_page();
                }
                @Override
                public void onDataReceivedFailed(String msg)
                {
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                    Log.e("课表爬虫", "爬取失败"+msg);
                }
            });
        }

        Log.e("高度",String.valueOf(height_per_lesson));
        update_page();

        ScrollView scrollview=findViewById(R.id.scrollview);
        scrollview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getPointerCount()==2)
                {
                    double y0 = motionEvent.getY(0);
                    double y1 = motionEvent.getY(1);
                    double distance=abs(y0-y1);
                    if(base_distance==0)
                    {
                        base_distance=distance;
                        base_dp=height_per_lesson;
                    }
                    else
                    {
                        height_per_lesson=(int)(base_dp*distance/base_distance);
                        if(height_per_lesson<150)
                            height_per_lesson=150;
                        if(height_per_lesson>240)
                            height_per_lesson=240;
                        tableView=findViewById(R.id.main_table);
                        tableView.setHeight_per_lesson(height_per_lesson);
                        tableView.setLessons(new LessonView.LessonClickListener<CustomLesson>() {
                            @Override
                            public void onClick(CustomLesson lesson) {
                                String msg="课程名："+lesson.getName()+"\n地点："+lesson.getPlace()+"\n时间："+lesson.getDate();
                                showDialog(msg);
                            }
                        });
                    }
                }
                else
                {
                    base_distance=0;
                }
                return false;
            }
        });
    }


    private List<CustomLesson> getCustomLessons(){
        List<CustomLesson> lessons=new ArrayList<>();
        lessons=Course_DB.course_getData(page);
        Log.e("课程信息",lessons.toString());
        return lessons;
    }
    private void showDialog(String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("课程信息");
        builder.setMessage(message);
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener() { //添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    public void cal_page_start()
    {
        //设置page_start为当前周
        try
        {
            String[] week_data=Course_DB.course_getData_week();
            Log.e("时间",week_data[2]);
            page_start=(int)course_util.getWeek(week_data);
            Log.e("page_start",String.valueOf(page_start));
        }
        catch(Exception e)
        {
            Log.e("查不到数据",e.toString());
            page_start=1;
        }
    }
    public void update_page()
    {
        List<Date> weekdays=course_util.GetWeekDays();
        Calendar cal=Calendar.getInstance();
        cal.setTime(weekdays.get(0));
        if(page!=page_start)
            page_num.setText("点击返回\n第"+String.valueOf(page)+"周\n"+cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1));
        else
            page_num.setText("当前周\n第"+String.valueOf(page)+"周\n"+cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1));
        tableView=findViewById(R.id.main_table);
        tableView.setLessons(getCustomLessons());
        tableView.setLessons(new LessonView.LessonClickListener<CustomLesson>() {
            @Override
            public void onClick(CustomLesson lesson) {
                String msg="课程名："+lesson.getName()+"\n地点："+lesson.getPlace()+"\n时间："+lesson.getDate();
                showDialog(msg);
            }
        });
    }
}
