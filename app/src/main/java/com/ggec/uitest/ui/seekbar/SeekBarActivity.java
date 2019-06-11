package com.ggec.uitest.ui.seekbar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Button;

import com.ggec.uitest.R;

public class SeekBarActivity extends FragmentActivity {
    private Device device;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        device = new Device("office", 30);
        setContentView(R.layout.activity_seek_bar);
        Button btnModifyVolume = findViewById(R.id.btn_activity_sk_modify_volume);
        btnModifyVolume.setOnClickListener(v -> device.setVolume(80));
        FragmentTransaction ft  =  getSupportFragmentManager().beginTransaction();
        SKFrameFragment fragment = new SKFrameFragment();
        ft.replace(R.id.sk_activity_frame, fragment).commit();
    }

    public Device getDevice() {
        return device;
    }
}
