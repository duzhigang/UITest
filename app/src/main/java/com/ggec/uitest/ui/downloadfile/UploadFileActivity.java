package com.ggec.uitest.ui.downloadfile;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ggec.uitest.R;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Created by ggec on 2018/12/12.
 * url = http://192.168.1.20:8080/handle_post_request
 * filePath = /storage/emulated/0/Android/data/com.ggec.wfspk/cache/files/1.00.166_vs_1.00.165.swu
 * fileName = 1.00.166_vs_1.00.165.swu
 * 目前下载几十KB就会出现BrokenPipe
 */

public class UploadFileActivity extends FragmentActivity {
    private static final String TAG = "UploadFileActivity";
    private static final int MSG_UPLOAD_FIRMWARE_START = 0x01;
    private static final int MSG_UPLOAD_FIRMWARE_PROGRESS = 0x02;
    private static final OkHttpClient okHttpClient = new OkHttpClient();
    private static final int SEGMENT_SIZE = 2*1024; // okio.Segment.SIZE

    private String directoryPath = "";
    private String filePath = "";
    private String fileName = "1.00.166_vs_1.00.165.swu";
    private String notifyUpdateUrl = "http://192.168.1.20/cgi-bin/wifi/wifi.cgi?cmd=runswupdate";
    private String uploadUrl = "http://192.168.1.20/:8080/handle_post_request";
    private long progress = 0;
    private TextView tvProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        File file = getApplication().getExternalCacheDir();
        if (file != null) {
            directoryPath = file.getAbsolutePath() + File.separator;
            Log.v(TAG,"directoryPath = " + directoryPath);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);
        TextView tvFileName = findViewById(R.id.tv_upload_file_name);
        tvFileName.setText(fileName);
        tvProgress = findViewById(R.id.tv_upload_file_progress);
        Button btnStart = findViewById(R.id.btn_upload_file_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkReadyUpload(notifyUpdateUrl);
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPLOAD_FIRMWARE_START:
                    run();
                    break;
                case MSG_UPLOAD_FIRMWARE_PROGRESS:
                    String result = progress + "%";
                    tvProgress.setText(result);
                    break;
            }
        }
    };

    private void run() {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        filePath = directoryPath + fileName;
        Log.v(TAG,"filePath = " + filePath);
        File file = new File(filePath);
        builder.addFormDataPart("file", fileName, createCustomRequestBody(MultipartBody.FORM, file, new ProgressListener() {
            @Override
            public void onProgress(long size) {
                progress = size;
                Log.i(TAG,"progress = " + progress);
            }
        }));

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .header("X_FILENAME","swu")
                .url(uploadUrl) //地址
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,"onFailure,上传失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"response.body().string() = " + response.body().string());
            }
        });
    }

    private static RequestBody createCustomRequestBody(final MediaType contentType, final File file, final ProgressListener listener) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(file);
                    long total = 0;
                    long read;
                    while ((read = source.read(sink.buffer(), SEGMENT_SIZE)) != -1) {
                        total += read;
                        sink.flush();
                        listener.onProgress(total);
                    }
                }
                catch (Exception e) {
                     e.printStackTrace();
                }
            }
        };
    }

    private void checkReadyUpload(String url) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.w(TAG,"onFailure()，上传镜像准备失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 延时1s，使设备准备好从手机端下载镜像
                    try {
                        Thread.sleep(1000);
                        Log.v(TAG,"onResponse()，上传镜像准备成功");
                        mHandler.sendEmptyMessage(MSG_UPLOAD_FIRMWARE_START);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.w(TAG,"onResponse()，上传镜像准备失败");
                }
            }
        });
    }

    interface ProgressListener {
        void onProgress(long size);
    }
}
