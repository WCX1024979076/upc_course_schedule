package com.example.upc.course;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.upc.R;
import java.util.ArrayList;
import java.util.List;
import com.example.upc.login.login_db;
import com.example.upc.util.AsyncResponse;
import com.example.upc.util.cookie;
import static java.lang.Integer.max;
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
    Button next=null,back=null;
    TextView page_num=null;
    ImageView imageview=null;
    static int page=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_course);
        context=this;
        Course_DB = new course_db(this);
        Login_DB= new login_db(this);

        if(cookie.path_cookie==null)
            cookie.path_cookie=this.getCacheDir()+"/cookie.txt";    ///获取缓存路径

        if(!Login_DB.login_check())
        {
            Toast.makeText(getApplicationContext(), "请先登录！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, com.example.upc.login.LoginActivity.class);
            startActivity(intent);
            return ;
        }
        //Course_DB.course_upgrade(); /// 更新数据库

        loadingProgressBar = findViewById(R.id.loading_course);
        next = findViewById(R.id.next);
        back = findViewById(R.id.back);
        imageview=findViewById(R.id.setting);
        page_num= findViewById(R.id.page);

        String[] week_data=Course_DB.course_getData_week();
        page=(int)course_util.getWeek(week_data);

        page_num.setText("第"+String.valueOf(page)+"周");
        next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                page++;
                page=min(page,18);
                page_num.setText("第"+String.valueOf(page)+"周");
                tableView=findViewById(R.id.main_table);
                tableView.setLessons(getCustomLessons(), new LessonView.LessonClickListener<CustomLesson>() {
                    @Override
                    public void onClick(CustomLesson lesson) {
                        String msg="课程名："+lesson.getName()+"\n地点："+lesson.getPlace()+"\n时间："+lesson.getDate();
                        showDialog(msg);
                    }
                });
            }
        });
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                page--;
                page=max(page,1);
                page_num.setText("第"+String.valueOf(page)+"周");
                tableView=findViewById(R.id.main_table);
                tableView.setLessons(getCustomLessons(), new LessonView.LessonClickListener<CustomLesson>() {
                    @Override
                    public void onClick(CustomLesson lesson) {
                        String msg="课程名："+lesson.getName()+"\n地点："+lesson.getPlace()+"\n时间："+lesson.getDate();
                        showDialog(msg);
                    }
                });
            }
        });
        imageview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.context, com.example.upc.setting.SettingActivity.class);
                startActivity(intent);
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
                    tableView=findViewById(R.id.main_table);
                    tableView.setLessons(getCustomLessons(), new LessonView.LessonClickListener<CustomLesson>() {
                        @Override
                        public void onClick(CustomLesson lesson) {
                            String msg="课程名："+lesson.getName()+"\n地点："+lesson.getPlace()+"\n时间："+lesson.getDate();
                            showDialog(msg);
                        }
                    });
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

        tableView=findViewById(R.id.main_table);
        tableView.setLessons(getCustomLessons(), new LessonView.LessonClickListener<CustomLesson>() {
            @Override
            public void onClick(CustomLesson lesson) {
                String msg="课程名："+lesson.getName()+"\n地点："+lesson.getPlace()+"\n时间："+lesson.getDate();
                showDialog(msg);
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
}
