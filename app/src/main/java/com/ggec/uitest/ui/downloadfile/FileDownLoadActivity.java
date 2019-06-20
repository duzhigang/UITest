package com.ggec.uitest.ui.downloadfile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ggec.uitest.R;
import com.ggec.uitest.application.MyApplication;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.io.File;

/**
 * Created by ggec on 2018/11/9.
 * 采用filedownloader库实现文件的断点下载，未下完之前是.temp文件，下载完后去掉.temp后缀。
 * 最新的1.7.5(1.7.0-1.7.5下载时，有些URL获取文件大小为-1)，支持断点下载
 */

public class FileDownLoadActivity extends FragmentActivity {
    private static final String TAG = "FileDownLoadActivity";

    public int singleTaskId = 0;
//    private String fileUrl = "http://iar.ggec.com.cn:3000/static/device_upgrade/swu/1.00.163.swu";
    private String fileUrl = "http://cdn.llsapp.com/android/LLS-v4.0-595-20160908-143200.apk";
    private String filePath = "";
    private String fileDirectory = "";

    private TextView tvSpeed;
    private TextView tvProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 打开FileDownloader库的Log开关
        FileDownloadLog.NEED_LOG = true;

        String fileName = getNameFromUrl(fileUrl);
        fileDirectory = MyApplication.getMyApplication().getExternalCacheDir()
                .getAbsolutePath() + File.separator + "files";
        filePath = fileDirectory + File.separator + fileName;
        Log.v(TAG,"fileDirectory = " + fileDirectory + "\nfilePath = " + filePath);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_file_download);
        TextView tvFileName = findViewById(R.id.tv_file_download_name);
        tvFileName.setText(fileName);
        tvSpeed = findViewById(R.id.tv_file_download_speed);
        tvProgress = findViewById(R.id.tv_file_download_progress);

        Button btnStart = findViewById(R.id.btn_file_download_start);
        btnStart.setOnClickListener(v -> {
            start_single(fileUrl);
        });

        Button btnPause = findViewById(R.id.btn_file_download_pause);
        btnPause.setOnClickListener(v -> pause_single());

        Button btnCancel = findViewById(R.id.btn_file_download_cancel);
        btnCancel.setOnClickListener(v -> delete_single());

    }

    private void start_single(String url) {
        BaseDownloadTask singleTask = FileDownloader.getImpl().create(url)
//                .setPath(filePath, false)
                // 如果pathAsDirectory是true,path就是存储下载文件的文件目录(而不是路径)，此时默认情况下文件名filename将会默认从response#header中的contentDisposition中获得
                .setPath(fileDirectory, true)
                .setCallbackProgressTimes(300)  // 下载过程中FileDownloadListener#progress最大回调次数
                .setMinIntervalUpdateSpeed(400) // 设置下载中刷新下载速度的最小间隔
                .setListener(new FileDownloadListener() {
                         @Override
                         protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                             Log.i(TAG, "pending taskId:" + task.getId() + ",soFarBytes:" + soFarBytes + ",totalBytes:" + totalBytes + ",percent:" + soFarBytes * 1.0 / totalBytes);
                         }

                         @Override
                         protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//                             Log.i(TAG, "progress taskId:" + task.getId() + ",soFarBytes:" + soFarBytes + ",totalBytes:" + totalBytes);
                             int progress = (int) (soFarBytes * 1.0 / totalBytes * 100);
                             tvProgress.setText(String.format("%d%%", progress));
                             updateSpeed(task.getSpeed());
                         }

                         @Override
                         protected void error(BaseDownloadTask task, Throwable e) {
                             Log.i(TAG, "error taskId:" + task.getId());
                         }

                         @Override
                         protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                             super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                             Log.i(TAG, "connected taskId:" + task.getId() + ",soFarBytes:" + soFarBytes + ",totalBytes:" + totalBytes + ",percent:" + soFarBytes * 1.0 / totalBytes);
                         }

                         @Override
                         protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                             Log.i(TAG, "paused taskId:" + task.getId() + ",soFarBytes:" + soFarBytes + ",totalBytes:" + totalBytes + ",percent:" + soFarBytes * 1.0 / totalBytes);
                         }

                         @Override
                         protected void completed(BaseDownloadTask task) {
                             Log.i(TAG, "completed taskId:" + task.getId());
                         }

                         @Override
                         protected void warn(BaseDownloadTask task) {
                             Log.i(TAG, "warn taskId:" + task.getId());
                         }
                     }
                );
        singleTaskId = singleTask.start();
    }

    public void pause_single(){
        Log.i(TAG,"pause_single task:"+singleTaskId);
        FileDownloader.getImpl().pause(singleTaskId);
    }

    public void delete_single(){
        new File(filePath).delete();
        new File(FileDownloadUtils.getTempPath(filePath)).delete();
    }

    // 判断当前文件是否存在，如存在给出提示，如不存在，开始下载
    // FileDownload库已经支持断点下载，只要本地存在该文件就不下载，所以不需要
    public boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    @NonNull
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    //更新下载速度
    private void updateSpeed(int speed) {
        tvSpeed.setText(String.format("%dKB/s", speed));
    }

}
