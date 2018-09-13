package com.ggec.uitest.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ggec.uitest.R;

/**
 * Created by ggec on 2018/9/11.
 * 测试将收到的URLEncode并转义之后的中应混合字符串显示在手机上
 */

public class ChineseCodeActivity extends FragmentActivity {
    private static final String TAG = "ChineseCodeActivity";

    // M测试test测试Wifi
    private String str = "M\\xe6\\xb5\\x8b\\xe8\\xaf\\x95test\\xe6\\xb5\\x8b\\xe8\\xaf\\x95Wifi";
//        String str = "m\\xe6\\xb5\\x8b\\xe8\\xaf\\x95Test";
//        String str = "\\xe6\\xb5\\x8b\\xe8\\xaf\\x95";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinese_code);
        str = str.replace("\\x", "");
        final TextView tvName = (TextView) findViewById(R.id.tv_chinese_code_name);
        Button btnShowChinese = (Button) findViewById(R.id.btn_chinese_code_show);
        btnShowChinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = hexStringToString(str);
                tvName.setText(result);
            }
        });
    }

    private String hexStringToString(String string) {
        if(string == null || string.equals("")) {
            return null;
        }

        while (true) {
            int start = string.indexOf("\\x");
            if (start == -1) {
                // 不存在\x
                break;
            }else {
                String remainStr = string.substring(start);

                int tempIndex = start + 4;
                while (tempIndex < string.length()) {
                    String tempStr = string.substring(tempIndex);
                    if (!tempStr.contains("\\x")) {
                        break;
                    } else {
                        tempIndex = tempIndex + 4;
                    }
                }
                String hexStr = string.substring(start, tempIndex-1);
                String chnStr = hexToString(hexStr);
                return chnStr;
            }
        }
        return null;
    }

    private String hexToString(String hexString) {
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return new String(d);
    }

    private byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
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
