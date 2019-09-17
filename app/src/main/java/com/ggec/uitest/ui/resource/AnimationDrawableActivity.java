package com.ggec.uitest.ui.resource;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.ggec.uitest.R;

public class AnimationDrawableActivity extends FragmentActivity {
    private static final String TAG = "AnimationDrawableAct";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreate().");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_drawable);
        final ImageView image = (ImageView)findViewById(R.id.iv_animation_drawable_act);
        // 加载动画资源
        final Animation anim = AnimationUtils.loadAnimation(this, R.anim.my_anim);
        // 设置动画结束后保留结束状态
        anim.setFillAfter(true);
        Button btn = findViewById(R.id.btn_animation_drawable_act_start);
        btn.setOnClickListener(v -> {
            // 开始动画
            image.startAnimation(anim);
        });
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
}
