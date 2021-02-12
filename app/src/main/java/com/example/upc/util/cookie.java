package com.example.upc.util;

import com.example.upc.login.LoginActivity;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

public class cookie
{
    public static String path_cookie=null;
    public static void Writecookies(Map cookies)
    {
        try
        {
            Log.e("写入cookie日志", "Create the cookie file"+path_cookie );
            File file = new File(path_cookie);
            if (!file.exists()) file.createNewFile();
            FileOutputStream outStream = new FileOutputStream(path_cookie);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
            objectOutputStream.writeObject(cookies);
            objectOutputStream.close();
            Log.e("写入cookie日志","成功写入cookie");
        }
        catch(Exception a){
            Log.e("写入cookie日志",a.toString());
        }
    }
    public static Map Readcookies()
    {
        Map cookies = null;
        try
        {
            FileInputStream inStream = new FileInputStream(path_cookie);
            ObjectInputStream objectInputStream = new ObjectInputStream(inStream);
            cookies=(Map) objectInputStream.readObject();
            objectInputStream.close();
        }
        catch(Exception a){
            Log.e("读取cookie",a.toString());
        }
        Log.e("读取cookie",cookies.toString());
        return cookies;
    }
}