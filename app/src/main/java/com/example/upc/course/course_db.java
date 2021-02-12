package com.example.upc.course;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;


public class course_db extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "upc_course.db";
    private static final String TABLE_NAME = "course_data";
    private static final String COLUMN1 = "course_id";
    private static final String COLUMN2 = "week";
    private static final String COLUMN3= "weekdays";
    private static final String COLUMN4 = "start_time";
    private static final String COLUMN5 = "end_time";
    private static final String COLUMN6= "course_name";
    private static final String COLUMN7 = "location";
    private static final String COLUMN8 = "date";
    private static final String COLUMN9= "color";
    private static final String COLUMN10= "teacher";
    private static final String COLUMN11= "day";
    public course_db(@Nullable Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        Log.e("新建数据库","开始");
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql="CREATE TABLE " + TABLE_NAME + "(course_id TEXT,week INT,weekdays INT,start_time INT,end_time INT,course_name TEXT,location TEXT,date TEXT,color INT,teacher TEXT,day TEXT)";
        Log.e("新建数据库表",sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean course_insert(String course_id,int week,int weekdays,int start_time,int end_time,String course_name,String location,int color,String date,String day)
    {
        Log.e("db插入数据",course_id+course_name+String.valueOf(week)+"上课"+String.valueOf(weekdays)+"开始"+String.valueOf(start_time)+"结束"+String.valueOf(end_time));
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN1, course_id);
        contentValues.put(COLUMN2, week);
        contentValues.put(COLUMN3, weekdays);
        contentValues.put(COLUMN4, start_time);
        contentValues.put(COLUMN5, end_time);
        contentValues.put(COLUMN6, course_name);
        contentValues.put(COLUMN7, location);
        contentValues.put(COLUMN8, date);
        contentValues.put(COLUMN9, color);
        contentValues.put(COLUMN11, day);
        long success = db.insert(TABLE_NAME, null, contentValues);
        if (success == -1){
            return false;
        } else {
            return true;
        }
    }

    public String[] course_getData_week()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT week,weekdays,day FROM "+TABLE_NAME,null);
        String week_data[]=new String[3];
        cursor.moveToNext();
        week_data[0]=cursor.getString(0);
        week_data[1]=cursor.getString(1);
        week_data[2]=cursor.getString(2);
        return week_data;
    }

    public List<CustomLesson> course_getData(int week)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        List<CustomLesson> lessons=new ArrayList<>();
        String year=course_util.getYear();
        String term=course_util.getTerm();
        Cursor cursor = db.rawQuery("SELECT course_name,start_time,end_time,weekdays,location,course_id,color,date FROM "+TABLE_NAME+" WHERE week="+String.valueOf(week),null);
        String[] login_data=new String[100];
        while(cursor.moveToNext())
        {
            int weekday=cursor.getInt(3);
            String weekday_str=null;
            if(weekday==0)
                weekday_str="sun";
            else if(weekday==1)
                weekday_str="mon";
            else if(weekday==2)
                weekday_str="tue";
            else if(weekday==3)
                weekday_str="wed";
            else if(weekday==4)
                weekday_str="thur";
            else if(weekday==5)
                weekday_str="fri";
            else if(weekday==6)
                weekday_str="sat";
            lessons.add(new CustomLesson(year+term,String.valueOf(week),cursor.getString(0),weekday_str,cursor.getInt(1),cursor.getInt(2),cursor.getString(4),cursor.getString(5),cursor.getInt(6),cursor.getString(7)));
        }
        cursor.close();
        return lessons;
    }

    public String[][] course_getData_upload(int week)
    {
        Log.e("获取信息","第"+String.valueOf(week)+"周");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT course_name,date,day,location FROM "+TABLE_NAME+" WHERE week="+String.valueOf(week),null);
        String[][] course_data_all=new String[200][4];
        int ans_sum=0;
        while(cursor.moveToNext())
        {
            String[] course_data=new String[4];
            course_data[0]=cursor.getString(0);
            course_data[1]=cursor.getString(2)+" "+cursor.getString(1).split("~")[0];
            course_data[2]=cursor.getString(2)+" "+cursor.getString(1).split("~")[1];
            course_data[3]=cursor.getString(3);
            course_data_all[ans_sum++]=course_data;
        }
        cursor.close();
        return course_data_all;
    }

    public boolean course_check()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + TABLE_NAME ;
        Cursor data = db.rawQuery(Query,null);
        if(data.getCount() <=0)
            return false;
        else
            return true;
    }

    public void course_update(String user_name,String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN1, user_name);
        contentValues.put(COLUMN2, password);
        db.update(TABLE_NAME, contentValues, "user_name = ?", new String[] {user_name});
    }

    public void course_upgrade()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Integer course_delete(String user_name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "user_name = ?", new String[] {user_name});
    }

    public Integer course_clear()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, null, null);
    }
}