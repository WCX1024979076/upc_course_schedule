package com.example.upc.setting;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.example.upc.course.course_db;
import com.example.upc.util.AsyncResponse;
class upload_spider extends AsyncTask<String, Void, String>
{
    public AsyncResponse asyncResponse;
    public void setOnAsyncResponse(AsyncResponse asyncResponse)
    {
        this.asyncResponse = asyncResponse;
    }
    @Override
    protected String doInBackground(String... ID) {
        try {
            String hostname = ID[0];
            int port = Integer.parseInt(ID[1]);
            String content=ID[2];
            String result="";
            PrintWriter out = null;
            BufferedReader in = null;
            Log.e("上传提醒爬虫","开始");
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(hostname,port), 10000);
            out = new PrintWriter(socket.getOutputStream());
            out.print(content);
            out.flush();
            return "操作成功";
        }
        catch (Exception a)
        {
            Log.e("上传提醒爬虫", a.toString());
            return a.toString();
        }
    }
    @Override
    protected void onPostExecute(String msg) {
        super.onPostExecute(msg);
        Log.e("上传提醒爬虫",msg);
        if (msg.equals("操作成功")) {
            asyncResponse.onDataReceivedSuccess(msg);//将结果传给回调接口中的函数
        }
        else {
            asyncResponse.onDataReceivedFailed(msg);
        }
    }
}
public class upload
{
    private String qq,minute;
    upload(String qq,int minute)
    {
        course_db Course_DB=new course_db(SettingActivity.context);
        Toast.makeText(SettingActivity.context, "开始提交提醒信息", Toast.LENGTH_SHORT).show();
        for(int week=1;week<=18;week++)
        {
            String[][] course_data_all=Course_DB.course_getData_upload(week);
            String ans="[";
            boolean flag=false;
            int size1=0;
            for(String[]  course_data : course_data_all)
            {
                if(course_data[0]==null)
                    break;
                if(!flag)
                {
                    ans+="{";
                    flag=true;
                }
                else
                    ans+=",{";
                ans+="'user':'"+qq+"',";
                ans+="'start':'"+course_data[1]+"',";
                ans+="'end':'"+course_data[2]+"',";
                ans+="'thing':'"+course_data[0]+"',";
                ans+="'note':'课表提醒',";
                ans+="'location':'"+course_data[3]+"',";
                ans+="'before':"+String.valueOf(minute)+"}";
                size1++;
                if(size1>=5)
                {
                    ans+="]";
                    Log.e("上传提醒爬虫",ans);
                    upload_tcp(ans);
                    flag=false;
                    size1=1;
                    ans="[";
                }
            }
            ans+="]";
            Log.e("上传提醒爬虫",ans);
            upload_tcp(ans);
        }
        Toast.makeText(SettingActivity.context, "上传成功", Toast.LENGTH_SHORT).show();
    }
    public void upload_tcp(String msg)
    {
        String [] array=new String[5];
        array[0]="8.131.54.221";
        array[1]="5050";
        array[2]=msg;
        upload_spider Upload_Spider = new upload_spider();
        Upload_Spider.execute(array);
        Upload_Spider.setOnAsyncResponse(new AsyncResponse() {
            @Override
            public void onDataReceivedSuccess(String msg)
            {
                Log.e("上传提醒爬虫", "上传成功");
            }
            @Override
            public void onDataReceivedFailed(String msg)
            {
                Toast.makeText(SettingActivity.context,msg,Toast.LENGTH_SHORT).show();
                Log.e("上传提醒爬虫", "上传失败"+msg);
            }
        });
    }
}