package com.example.upc.login;
import android.os.AsyncTask;
import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.Map;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import com.example.upc.util.AsyncResponse;
import com.example.upc.util.cookie;
public class login_spider extends AsyncTask<String, Void, String>
{
    public AsyncResponse asyncResponse;
    public void setOnAsyncResponse(AsyncResponse asyncResponse)
    {
        this.asyncResponse = asyncResponse;
    }
    @Override
    protected String doInBackground(String... ID) {
        try {
            String user_name = ID[0];
            String password = ID[1];
            Log.e("登陆爬虫","开始登陆，用户名"+user_name+"密码"+password);
            Response response = Jsoup.connect("https://app.upc.edu.cn/uc/wap/login/check")
                    .userAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.15)")
                    .method(Method.POST)
                    .data("username", user_name)
                    .data("password", password)
                    .ignoreContentType(true)
                    .execute();
            Map cookies = response.cookies();
            Document document = response.parse();
            cookie.Writecookies(cookies);
            Log.e("登录日志", cookies.toString());
            Log.e("登录日志", document.toString());
            if (document.toString().contains("操作成功"))
            {
                return "操作成功";
            }
            else
            {
                return "密码错误";
            }
        } catch (Exception a)
        {
            Log.e("登录日志", a.toString());
            return a.toString();
        }
    }
    @Override
    protected void onPostExecute(String msg) {
        super.onPostExecute(msg);
        Log.e("登陆爬虫",msg);
        if (msg.equals("操作成功")) {
            asyncResponse.onDataReceivedSuccess(msg);//将结果传给回调接口中的函数
        }
        else {
            asyncResponse.onDataReceivedFailed(msg);
        }
    }
}
