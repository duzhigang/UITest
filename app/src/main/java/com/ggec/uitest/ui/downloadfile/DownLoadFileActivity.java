package com.ggec.uitest.ui.downloadfile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ggec.uitest.R;
import com.ggec.uitest.application.MyApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ggec on 2018/9/14.
 * 测试用okHttp下载文件到包里面并扫描显示当前包目录下所有以.swu结尾的文件
 */

public class DownLoadFileActivity extends FragmentActivity {
    private static final String TAG = "DownLoadFileActivity";
    private static final int MSG_DOWNLOAD_SUCCESS = 0x01;
    private static final int MSG_DOWNLOAD_FAILED = 0x02;
    private static final int MSG_DOWNLOAD_PROGRESS = 0x03;

//    private String url = "http://iar.ggec.com.cn:3000/swu/6_m_a_20180726202638.swu";    //77kB
    private String url = "http://iar.ggec.com.cn:3000/static/device_upgrade/swu/1.00.163.swu";    //50MB
//    private String url = "http://iar.ggec.com.cn:3000/swu/6_m_n_20180522194109.swu";    //11.3MB
    private TextView tvProgress;
    private List<String> fileNames = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        /**
         * 动态获取权限，Android 6.0 新特性，一些保护权限，除了要在AndroidManifest中声明权限，还要使用如下代码动态获取
         */
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_file);
        TextView tvFileName = (TextView) findViewById(R.id.tv_download_file_name);
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        tvFileName.setText(fileName);
        tvProgress = (TextView) findViewById(R.id.tv_download_file_progress);
        Button btnStart = (Button) findViewById(R.id.btn_download_file_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new Progress().start(url);
                DownloadUtil.get().download(url, "files", new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess() {
                        mHandler.sendEmptyMessage(MSG_DOWNLOAD_SUCCESS);
                    }

                    @Override
                    public void onDownloading(int progress) {
                        mHandler.obtainMessage(MSG_DOWNLOAD_PROGRESS, progress).sendToTarget();
                    }

                    @Override
                    public void onDownloadFailed() {
                        mHandler.sendEmptyMessage(MSG_DOWNLOAD_FAILED);
                    }
                });
            }
        });

        Button btnScan = (Button) findViewById(R.id.btn_download_file_scan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFileNames();
                adapter.notifyDataSetChanged();
            }
        });
        ListView lvFiles = (ListView) findViewById(R.id.lv_download_file_names);
        adapter = new ArrayAdapter<String>(this, R.layout.one_item_left, R.id.tv_one_item_left_name, fileNames);
        lvFiles.setAdapter(adapter);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_DOWNLOAD_SUCCESS:
                    Toast.makeText(DownLoadFileActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_DOWNLOAD_FAILED:
                    Toast.makeText(DownLoadFileActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_DOWNLOAD_PROGRESS:
                    int progress = (int) msg.obj;
                    tvProgress.setText(String.valueOf(progress));
                    break;
                default:
                    break;
            }

        }
    };

    private void getFileNames() {
        String filePath = MyApplication.getMyApplication().getExternalFilesDir(null).getAbsolutePath() + "/images";
        List<File> files = getSuffixFiles(filePath, ".swu");
        for (File file : files) {
            fileNames.add(file.getName());
        }
    }

    @Nullable
    private List<File> getSuffixFiles(String filePath, String suffix) {
        List<File> files = new ArrayList<>();
        File f = new File(filePath);
        if (! f.exists()) {
            return null;
        }
        File[] subFiles = f.listFiles();
        for (File subFile : subFiles) {
            if (subFile.isFile() && subFile.getName().endsWith(suffix)) {
                files.add(subFile);
                Log.e(TAG,"找到的文件为：" + subFile.getName());
            }
        }
        return files;
    }
}
