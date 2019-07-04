package com.ggec.uitest.ui.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";
    public static final String CREATE_STUDENT = "create table student ("
            + "name text primary key, "
            + "age integer, "
            + "sex text)";

    // name表示数据库的名字
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STUDENT);
        Log.i(TAG,"onCreate，新建数据库");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG,"onUpgrade，更新数据库");
        // 如果发现数据库中已经存在student表了，就将这张表删除掉，然后再调用onCreate()方法去重新创建
        db.execSQL("drop table if exists student");
        onCreate(db);
    }
}
