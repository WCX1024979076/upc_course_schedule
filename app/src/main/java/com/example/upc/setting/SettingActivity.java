package com.example.upc.setting;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.upc.R;
import com.example.upc.course.course_db;
import com.example.upc.course.course_spider;
import com.example.upc.login.LoginActivity;
import com.example.upc.login.login_db;
import com.example.upc.login.login_spider;
import com.example.upc.util.AsyncResponse;
import com.example.upc.util.cookie;

import static com.example.upc.login.LoginActivity.Login_db;


public class SettingActivity extends AppCompatActivity
{
    Button logout=null;
    Button update=null;
    Button go_back=null;
    Button remaind=null;
    public static Context context=null;
    ProgressBar loadingProgressBar=null;
    CreateUserDialog createUserDialog=null;
    public static Activity activity=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_setting);
        logout = findViewById(R.id.logout);
        update = findViewById(R.id.update);
        remaind=findViewById(R.id.remaind);
        loadingProgressBar=findViewById(R.id.loading_setting);
        go_back= findViewById(R.id.go_back);
        context = this;
        activity=this;

        if(cookie.path_cookie==null)
            cookie.path_cookie=this.getCacheDir()+"/cookie.txt";    ///获取缓存路径

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("退出按钮", "点击");
                Log.e("删库跑路", "开始");
                login_db Login_db = new login_db(SettingActivity.context);
                Login_db.login_clear();
                course_db Course_db = new course_db(SettingActivity.context);
                Course_db.course_clear();
                Intent intent = new Intent(SettingActivity.context, com.example.upc.login.LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("返回按钮", "点击");
                Intent intent = new Intent(SettingActivity.context, com.example.upc.course.MainActivity.class);
                startActivity(intent);
            }
        });
        remaind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("提醒按钮", "点击");
                createUserDialog= new CreateUserDialog(SettingActivity.activity,onClickListener);
                createUserDialog.show();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("更新按钮", "点击");
                login_db Login_db=new login_db(SettingActivity.context);
                Toast.makeText(getApplicationContext(), "开始登陆", Toast.LENGTH_SHORT).show();
                loadingProgressBar.setVisibility(View.VISIBLE);
                String[] login_data=Login_db.login_getData();
                login_spider Login_Spider = new login_spider();
                Login_Spider.execute(login_data);
                Login_Spider.setOnAsyncResponse(new AsyncResponse() {
                    @Override
                    public void onDataReceivedSuccess(String msg)
                    {
                        loadingProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onDataReceivedFailed(String msg)
                    {
                        loadingProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                        Log.e("登录爬虫", "登陆失败");
                    }
                });

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
        });
    }
    public class CreateUserDialog extends Dialog {
        
        Activity context;
        private Button btn_save;
        public EditText text_qq;
        public EditText text_minute;
        
        private View.OnClickListener mClickListener;

        public CreateUserDialog(Activity context) {
            super(context);
            this.context = context;
        }

        public CreateUserDialog(Activity context, View.OnClickListener clickListener) {
            super(context);
            this.context = context;
            this.mClickListener = clickListener;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // 指定布局
            this.setContentView(R.layout.create_user_dialog);
            text_qq = (EditText) findViewById(R.id.text_qq);
            text_minute = (EditText) findViewById(R.id.text_minute);
            /*
             * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
             * 对象,这样这可以以同样的方式改变这个Activity的属性.
             */
            Window dialogWindow = this.getWindow();

            WindowManager m = context.getWindowManager();
            Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
            WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
            p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
            dialogWindow.setAttributes(p);
            // 根据id在布局中找到控件对象
            btn_save = (Button) findViewById(R.id.btn_save_pop);
            // 为按钮绑定点击事件监听器
            btn_save.setOnClickListener(mClickListener);
            this.setCancelable(true);
        }
    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_save_pop:
                    String qq = createUserDialog.text_qq.getText().toString().trim();
                    String minute = createUserDialog.text_minute.getText().toString().trim();
                    Log.e("提交界面",qq+minute);
                    upload Upload=new upload(qq,Integer.parseInt(minute));
                    createUserDialog.dismiss();
                    break;
            }
        }
    };
}
