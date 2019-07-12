package com.ggec.uitest.ui.md5;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.ggec.uitest.R;
import com.ggec.uitest.application.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Activity extends FragmentActivity {
    private static final String TAG = "Md5Activity";

    private TextView tvResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreate().");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_md5);
        findViewById(R.id.btn_act_md5_file_value).setOnClickListener(v -> {
            File file = MyApplication.getMyApplication().getExternalCacheDir();
            if (file == null) return;
            String fileDir = file.getAbsolutePath() + File.separator;
            String fileName = "test.txt";
            File testFile = createNewFile(fileDir, fileName);
//            File file = new File("/storage/emulated/0/Android/data/");
//            String fileMd5Str = getFileMD5One(testFile);
            String fileMd5Str = getFileMD5Two(testFile);
            tvResult.setText(fileMd5Str);
        });
        findViewById(R.id.btn_act_md5_string_value).setOnClickListener(v -> {
            String str = "This is a string for md5 test";
            String stringMD5Str = getStringMD5(str);
            tvResult.setText(stringMD5Str);
        });
        tvResult = findViewById(R.id.tv_act_md5_result);
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

    @NonNull
    private File createNewFile(String dir, String name) {
        String testStr = "This is a file for md5 test";
        String filePath = dir + name;

        // 使用FileWriter
        FileWriter writer;
        try {
            writer = new FileWriter(filePath);
            writer.write(testStr);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

/*        // 使用FileOutPutStream
        File file = new File(filePath);
        if (! file.exists()) {
            try {
                file.createNewFile();
                byte bytes[] = testStr.getBytes();;
                int b = bytes.length; // 是字节的长度，不是字符串的长度
                FileOutputStream fos=new FileOutputStream(file);
                fos.write(bytes,0,b);
                fos.write(bytes);
                fos.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }*/

/*        // 使用FileOutPutStream追加写入文件
        try {
            // true表示在文件末尾追加
            FileOutputStream fos = new FileOutputStream(filePath,true);
            fos.write(testStr.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        return new File(filePath);
    }

    public String getFileMD5One(File file) {
        BigInteger bi = null;
        try {
            byte[] buffer = new byte[8192];
            int len;
            MessageDigest md = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream(file);
            while ((len = fis.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            fis.close();
            byte[] b = md.digest();
            bi = new BigInteger(1, b);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        if(bi != null)
            return bi.toString(16);
        else
            return "";
    }

    public String getFileMD5Two(File file) {
        if (file == null || !file.isFile() || !file.exists()) {
            return "";
        }
        FileInputStream in = null;
        StringBuilder result = new StringBuilder();
        byte buffer[] = new byte[8192];
        int len;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                md5.update(buffer, 0, len);
            }
            byte[] bytes = md5.digest();

            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(null!=in){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }

    public String getStringMD5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
