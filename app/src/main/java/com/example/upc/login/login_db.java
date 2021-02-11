package com.example.upc.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;


public class login_db extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "upc.db";
    private static final String TABLE_NAME = "login_data";
    private static final String COLUMN1 = "user_name";
    private static final String COLUMN2 = "password";

    public login_db(@Nullable Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(user_name TEXT,password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean login_insert(String user_name,String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN1, user_name);
        contentValues.put(COLUMN2, password);
        long success = db.insert(TABLE_NAME, null, contentValues);
        if (success == -1){
            return false;
        } else {
            return true;
        }
    }

    public String[] login_getData()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        String[] login_data=new String[2];
        while(cursor.moveToNext())
        {
            String user_name=cursor.getString(0);
            String password=cursor.getString(1);
            login_data[0]=user_name;
            login_data[1]=password;
        }
        cursor.close();
        return login_data;
    }

    public boolean login_check()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + TABLE_NAME ;
        Cursor data = db.rawQuery(Query,null);
        if(data.getCount() <=0)
            return false;
        else
            return true;
    }

    public void login_update(String user_name,String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN1, user_name);
        contentValues.put(COLUMN2, password);
        db.update(TABLE_NAME, contentValues, "user_name = ?", new String[] {user_name});
    }

    public Integer login_delete(String user_name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "user_name = ?", new String[] {user_name});
    }

    public Integer login_clear()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, null, null);
    }
}