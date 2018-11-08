package com.ggec.uitest.ui.stringcode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ggec.uitest.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by ggec on 2018/9/11.
 * 测试将收到的URLEncode并转义之后的中应混合字符串显示在手机上
 */

public class ChineseCodeActivity extends FragmentActivity {
    private static final String TAG = "ChineseCodeActivity";

    // M测试t测试Wifi
    private String str = "M\\xe6\\xb5\\x8b\\xe8\\xaf\\x95test\\xe6\\xb5\\x8b\\xe8\\xaf\\x95Wifi";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinese_code);
        final TextView tvName = (TextView) findViewById(R.id.tv_chinese_code_name);
        tvName.setText(str);
        Button btnShowChinese = (Button) findViewById(R.id.btn_chinese_code_show);
        btnShowChinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = hexStringToString(str);
                Log.e(TAG,"result = " + result);
                tvName.setText(result);
            }
        });
    }

    private String hexStringToString(String str) {
        String decodeStr = null;
        str = str.replace("\\x", "%");
        try {
            decodeStr = URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodeStr;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG,"onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG,"onDestroy()");
    }
}
