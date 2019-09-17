package com.ggec.uitest.ui.resource;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;

import com.ggec.uitest.R;

import java.io.IOException;

/**
 * 测试使用原始资源raw、assets
 * */
public class RawAssetsActivity extends FragmentActivity {
    private static final String TAG = "RawAssetsActivity";

    private MediaPlayer rawPlayer = null;
    private MediaPlayer assetsPlayer = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_assets);
        Button btnPlayRaw = findViewById(R.id.btn_raw_assets_act_play_raw);
        btnPlayRaw.setOnClickListener(v -> {
            // 播放声音
            rawPlayer.start();
        });

        Button btnPlayAssets = findViewById(R.id.btn_raw_assets_act_play_assets);
        btnPlayAssets.setOnClickListener(v -> {
            // 播放声音
            assetsPlayer.start();
        });

        // 直接根据声音文件的ID来创建MediaPlayer
        rawPlayer = MediaPlayer.create(this, R.raw.bomb);
        // 获取该应用的AssetManager
        AssetManager am = getAssets();
        try {
            // 获取指定文件对应的AssetFileDescriptor
            AssetFileDescriptor afd = am.openFd("shot.mp3");
            assetsPlayer = new MediaPlayer();
            // 使用MediaPlayer加载指定的声音文件，必须要带offset和length参数，否则会抛Exception
            assetsPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getStartOffset());
            assetsPlayer.prepare();
        }
        catch (IOException e) {
            e.printStackTrace();
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
        rawPlayer.reset();
        rawPlayer.release();
        rawPlayer = null;

        assetsPlayer.reset();
        assetsPlayer.release();
        assetsPlayer = null;
    }
}
