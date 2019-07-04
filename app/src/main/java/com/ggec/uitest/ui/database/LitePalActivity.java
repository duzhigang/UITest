package com.ggec.uitest.ui.database;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ggec.uitest.R;

import org.litepal.LitePal;

import java.util.List;

/**
 * 测试Android中用的较多的数据库开源库(最终用的还是SQLite库)LitePal https://github.com/LitePalFramework/LitePal
 * LitePal的使用：https://www.jianshu.com/p/9d0d00b69fe8
 * 升级数据库，只需要修改litepal.xml中的version以及数据模型
 * */
public class LitePalActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "LitePalActivity";

    private EditText etInsert, etUpdateBefore, etUpdateAfter, etDelete;
    private TextView tvResult;

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
        // 如果表格中有该包含该name的数据，则不会添加；否则会在后面自动插入
        StudentLitePalBean stu1 = new StudentLitePalBean();
        stu1.setName("小明");
        stu1.setAge(11);
        stu1.setSex("male");
        stu1.save();
        StudentLitePalBean stu2 = new StudentLitePalBean();
        stu2.setName("小红");
        stu2.setAge(12);
        stu2.setSex("female");
        stu2.save();
        StudentLitePalBean stu3 = new StudentLitePalBean();
        stu3.setName("小张");
        stu3.setAge(13);
        stu3.setSex("male");
        stu3.save();
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
        switch (v.getId()) {
            case R.id.btn_act_database_insert:
                // 插入数据
                String insertAuth = etInsert.getText().toString();
                StudentLitePalBean stu1 = new StudentLitePalBean();
                stu1.setName(insertAuth);
                stu1.setAge(21);
                stu1.setSex("insertSex");
                boolean isSaved = stu1.save();
                if (isSaved) {
                    Toast.makeText(this, "插入成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "插入失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_act_database_insert_clear:
                etInsert.setText("");
                break;
            case R.id.btn_act_database_update:
                // 更新数据
                String updateDataBefore = etUpdateBefore.getText().toString();
                String updateDataAfter = etUpdateAfter.getText().toString();
/*                // 通过id来查找对象
                StudentLitePalBean stu2 = LitePal.find(StudentLitePalBean.class, 1);
                stu2.setName(updateDataAfter);
                stu2.save();*/
                StudentLitePalBean stu2 = new StudentLitePalBean();
                stu2.setName(updateDataAfter);
                stu2.setAge(31);
                stu2.setSex("updateSex");
                // 返回总共更新的行数
                int countUpdate = stu2.updateAll("name = ?", updateDataBefore);
                if (countUpdate == -1) {
                    Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "更新成功:" + countUpdate, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_act_database_update_clear:
                etUpdateBefore.setText("");
                etUpdateAfter.setText("");
                break;
            case R.id.btn_act_database_delete:
                // 删除数据
                String deleteAuth = etDelete.getText().toString();
                // 通过id来删除对象
//                LitePal.delete(StudentLitePalBean.class, 1);
                // 返回总共删除的行数
                int countDelete = LitePal.deleteAll(StudentLitePalBean.class, "name = ?", deleteAuth);
                Toast.makeText(this, "更新的列数为:" + countDelete, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_act_database_delete_clear:
                etDelete.setText("");
                break;
            case R.id.btn_act_database_upgrade:
                // 升级数据库，只需要修改litepal.xml中的version以及数据模型
                Toast.makeText(this, "暂未实现", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_act_database_query_all:
                // 通过id来查找对象
//                StudentLitePalBean studentLitePalBeans = LitePal.find(StudentLitePalBean.class, 1);
                // 查询数据库里面的所有内容
                List<StudentLitePalBean> studentLitePalBeans = LitePal.findAll(StudentLitePalBean.class);
                String result = "";
                result = result + "版本号：" + LitePal.getDatabase().getVersion();
                for (StudentLitePalBean stu : studentLitePalBeans) {
                    String name = stu.getName();
                    int age = stu.getAge();
                    String sex = stu.getSex();
                    result = result + "\n" + name + ", " + age + ", " + sex;
                }
                tvResult.setText(result);
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
