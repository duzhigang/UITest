package com.ggec.uitest.ui.language;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ggec.uitest.R;
import com.ggec.uitest.application.MyApplication;

import java.util.Locale;

/*
*  修改APP的语言，只是将语言配置保存在APP里面，与系统语言无关
* */

public class ChangeAppLanguageActivity extends FragmentActivity {
    private static final String TAG = "ChangeAppLanguageAct";

    private String language = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_app_language);
        Button btnChange = findViewById(R.id.btn_act_change_app_language_start);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListDialog();
            }
        });
    }

    private void showListDialog() {
        final String[] items = {getString(R.string.chinese_language), getString(R.string.english_language) };
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(this);
        listDialog.setTitle(R.string.language_type);
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // which 下标从0开始
                if (which == 0) {
                    language = "ch";
                } else {
                    language = "en";
                }
                SharedPreferences pre = getSharedPreferences("LanguageInput", MODE_PRIVATE);
                SharedPreferences.Editor editor = pre.edit();
                editor.putString("languageType",language);
                editor.apply();
                Log.v(TAG,"选择的语言:" + language);
                Toast.makeText(ChangeAppLanguageActivity.this, "你点击了" + items[which], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangeAppLanguageActivity.this, ChangeAppLanguageActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        listDialog.show();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        //获取preferences和editor对象,必须先获得context对象才可以执行getSharedPreferences，否则context为空
        MyApplication myApplication = MyApplication.getMyApplication();
        SharedPreferences pre = myApplication.getSharedPreferences("LanguageInput", MODE_PRIVATE);
        String sysLanguage = Locale.getDefault().getLanguage();
        Log.v(TAG,"APP缓存中的language = " + language);
        language =  pre.getString("languageType",sysLanguage);
        Log.v(TAG,"APP缓存中的language = " + language + ",系统中的language = " + sysLanguage);
        Locale locale = new Locale(language);
        final Resources res = newBase.getResources();
        final Configuration config = res.getConfiguration();
        config.setLocale(locale); // getLocale() should return a Locale
        final Context newContext = newBase.createConfigurationContext(config);
        super.attachBaseContext(newContext);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG,"onStart");
    }

    @Override
    public void recreate() {
        super.recreate();
        Log.v(TAG,"recreate");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v(TAG,"onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG,"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG,"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG,"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG,"onDestroy");
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        Log.v(TAG,"onAttachFragment");
    }
}
