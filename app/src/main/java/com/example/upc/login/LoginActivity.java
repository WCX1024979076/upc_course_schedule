package com.example.upc.login;

import android.content.Intent;
import android.os.Bundle;
import java.io.*;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.example.upc.R;
import com.example.upc.util.*;

public class LoginActivity extends AppCompatActivity
{
    EditText usernameEditText=null,passwordEditText=null;
    Button loginButton=null;
    ProgressBar loadingProgressBar=null;

    public static login_db Login_db=null;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Login_db=new login_db(com.example.upc.login.LoginActivity.this);

        if(cookie.path_cookie==null)
            cookie.path_cookie=this.getCacheDir()+"/cookie.txt";    ///获取缓存路径

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);
        loginButton.setOnClickListener(new com.example.upc.login.LoginActivity.login_Button_listener());

        login();    ///判断数据库中是否有登陆数据，如果有，直接登录
    }
    private class login_Button_listener implements View.OnClickListener   //为按钮点击事件提交
    {
        @Override
        public void onClick(View view)
        {
            Log.e("登录按钮","点击");
            String user_name=usernameEditText.getText().toString();
            String password=passwordEditText.getText().toString();
            Login_db.login_clear();
            Login_db.login_insert(user_name,password);
            login();
        }
    }
    public void login()
    {
        if(Login_db.login_check())
        {
            Log.e("登陆爬虫","开始");
            Toast.makeText(getApplicationContext(), "开始登陆", Toast.LENGTH_SHORT).show();
            loadingProgressBar.setVisibility(View.VISIBLE);
            String[] login_data=Login_db.login_getData();
            usernameEditText.setText(login_data[0]);
            passwordEditText.setText(login_data[1]);
            login_spider Login_Spider = new login_spider();
            Login_Spider.execute(login_data);
            Login_Spider.setOnAsyncResponse(new AsyncResponse() {
                @Override
                public void onDataReceivedSuccess(String msg)
                {
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                    Log.e("登陆爬虫", "跳转界面");
                    Intent intent = new Intent(LoginActivity.this, com.example.upc.course.MainActivity.class);
                    startActivity(intent);
                }
                @Override
                public void onDataReceivedFailed(String msg)
                {
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                    Log.e("登录爬虫", "登陆失败");
                    Login_db.login_clear();
                }
            });
        }
    }
    private void showDialog(String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(message);
        AlertDialog dialog=builder.create();
        dialog.show();
    }
}
