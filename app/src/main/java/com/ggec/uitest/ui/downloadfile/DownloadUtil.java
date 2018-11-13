package com.ggec.uitest.ui.downloadfile;

import android.support.annotation.NonNull;
import android.util.Log;

import com.ggec.uitest.application.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ggec on 2018/9/14.
 */

public class DownloadUtil {
    private static final String TAG = "DownloadUtil";

    private static DownloadUtil downloadUtil;
    private final OkHttpClient okHttpClient;

    public static DownloadUtil get() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtil();
        }
        return downloadUtil;
    }

    private DownloadUtil() {
        okHttpClient = new OkHttpClient();
    }

    /**
     * @param url 下载连接
     * @param saveDir 储存下载文件的SDCard目录
     * @param listener 下载监听
     */
    public void download(final String url, final String saveDir, final OnDownloadListener listener) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "下载失败");
                // 下载失败
                listener.onDownloadFailed();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "onResponse");
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录(绝对路径)
                String savePath = isExistDir(saveDir);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath, getNameFromUrl(url));
                    // 构建FileOutputStream对象,文件不存在会自动新建
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        listener.onDownloading(progress);
                    }
                    fos.flush();
                    // 下载完成
                    listener.onDownloadSuccess();
                } catch (Exception e) {
                    listener.onDownloadFailed();
                } finally {
                    if (is != null)
                    {
                        is.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                }
            }
        });
    }

    /**
     * @param saveDir
     * @return String
     * @throws IOException
     * 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        // 得到的是/storage/emulated/0/Android/data/包名/cache/目录
        File downloadFile = new File(MyApplication.getMyApplication().getExternalCacheDir(), saveDir);
        //  得到的是/saveDir
//        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        // 在指定位置创建目录，这会创建路径中所有不存在的目录
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        // 获取文件的绝对路径，与文件是否存在没关系
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    /**
     * @param url
     * @return
     * 从下载连接中解析出文件名
     */
    @NonNull
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * @param progress
         * 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }
}
