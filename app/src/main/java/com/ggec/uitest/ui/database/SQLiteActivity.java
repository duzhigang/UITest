package com.ggec.uitest.ui.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ggec.uitest.R;

/**
 * 测试Android自带的SQLite数据库
* SQLite数据库基本用法详解：https://blog.csdn.net/midnight_time/article/details/80834198
* */
public class SQLiteActivity extends FragmentActivity  implements View.OnClickListener {
    private static final String TAG = "SQLiteActivity";

    private EditText etInsert, etUpdateBefore, etUpdateAfter, etDelete;
    private TextView tvResult;
    private DBHelper dbHelper;
    private int version = 1;
    private String dbName = "sqLite_test.db";
    private String[] names = {"小明", "小红", "小张"};
    private int[] ages = {11, 12, 13};
    private String[] sex = {"male", "female", "male"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreate().");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        etInsert = findViewById(R.id.et_act_database_insert);
        findViewById(R.id.btn_act_database_insert).setOnClickListener(this);
        findViewById(R.id.btn_act_database_insert_clear).setOnClickListener(this);

        etUpdateBefore = findViewById(R.id.et_act_database_update_before);
        etUpdateAfter = findViewById(R.id.et_act_database_update_after);
        findViewById(R.id.btn_act_database_update).setOnClickListener(this);
        findViewById(R.id.btn_act_database_update_clear).setOnClickListener(this);

        etDelete = findViewById(R.id.et_act_database_delete);
        findViewById(R.id.btn_act_database_delete).setOnClickListener(this);
        findViewById(R.id.btn_act_database_delete_clear).setOnClickListener(this);

        findViewById(R.id.btn_act_database_upgrade).setOnClickListener(this);
        findViewById(R.id.btn_act_database_query_all).setOnClickListener(this);
        findViewById(R.id.btn_act_database_clear_query).setOnClickListener(this);
        tvResult = findViewById(R.id.tv_act_database_result);

        initDB();
    }

    private void initDB() {
        //依靠DatabaseHelper的构造函数创建数据库
        dbHelper = new DBHelper(this, dbName,null,version);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db != null) {
            for (int i = 0; i < names.length; i++) {
                ContentValues values = new ContentValues();
                values.put("name", names[i]);
                values.put("age", ages[i]);
                values.put("sex", sex[i]);
                long count = db.insert("student", null, values);
                if (count != -1) {
                    Log.i(TAG,"初始化时插入数据成功:" + count);
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG,"onStart().");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG,"onResume().");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG,"onPause().");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG,"onStop().");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG,"onDestroy().");
    }

    @Override
    public void onClick(View v) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (v.getId()) {
            case R.id.btn_act_database_insert:
                // 插入数据
                ContentValues values = new ContentValues();
                String insertAuth = etInsert.getText().toString();
                values.put("name", insertAuth);
                values.put("age", 21);
                values.put("sex", "insertSex");
                if (db != null) {
                    long count = db.insert("student", null, values);
                    if (count == -1) {
                        Toast.makeText(this, "插入失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "插入的位置为:" + count, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_act_database_insert_clear:
                etInsert.setText("");
                break;
            case R.id.btn_act_database_update:
                // 更新数据
                String updateDataBefore = etUpdateBefore.getText().toString();
                String updateDataAfter = etUpdateAfter.getText().toString();
                ContentValues valuesUpdate = new ContentValues();
                valuesUpdate.put("name", updateDataAfter);
                // 后面的这些成员变量可以不更改
                valuesUpdate.put("age",31);
                valuesUpdate.put("sex","updateSex");
                if (db != null) {
                    long count = db.update("student", valuesUpdate, "name = ?", new String[] {updateDataBefore});
                    if (count == -1) {
                        Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_act_database_update_clear:
                etUpdateBefore.setText("");
                etUpdateAfter.setText("");
                break;
            case R.id.btn_act_database_delete:
                // 删除数据
                String deleteAuth = etDelete.getText().toString();
                if (db != null) {
                    long count = db.delete("student", "name = ?", new  String[] {deleteAuth});
                    Log.i(TAG,"删除的行数count = " + count);
                    if (count == -1) {
                        Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_act_database_delete_clear:
                etDelete.setText("");
                break;
            case R.id.btn_act_database_upgrade:
                // 升级数据库
                version = 2;
                dbHelper = new DBHelper(this, dbName,null,version);
                break;
            case R.id.btn_act_database_query_all:
                // 查询数据库里面的所有内容
                if (db == null) break;
                //创建游标对象
                Cursor cursor = db.query("student", null, null, null, null, null, null);
                //利用游标遍历所有数据对象
                //为了显示全部，把所有对象连接起来，放到TextView中
                String result = "所有信息如下：\n";
                result = result + "数据库名字：" + dbHelper.getDatabaseName() + ",版本号：" + db.getVersion();
                while(cursor.moveToNext()){
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    int age = cursor.getInt(cursor.getColumnIndex("age"));
                    String sex = cursor.getString(cursor.getColumnIndex("sex"));
                    result = result + "\n" + name + ", " + age  + ", " + sex ;
                }
                tvResult.setText(result);
                // 关闭游标，释放资源
                cursor.close();
                break;
            case R.id.btn_act_database_clear_query:
                tvResult.setText("");
                tvResult.setHint("查询内容为空");
                break;
            default:
                break;
        }
    }
}
