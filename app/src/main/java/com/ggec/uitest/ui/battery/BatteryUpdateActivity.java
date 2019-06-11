package com.ggec.uitest.ui.battery;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ggec.uitest.R;

/**
 * Created by ggec on 2019/3/28.
 * 测试电池电量的显示与更新
 * 采用两种方法来实现：
 * (1).通过自定义View，手动绘制
 * (2).通过使用ProgressBar控件，不同的电量区间设置不同的Drawable
 * */
public class BatteryUpdateActivity extends FragmentActivity {
    private static final String TAG = "BatteryUpdateActivity";
    private static final int MSG_UPDATE_VIEW = 0x01;

    private BatteryView bvBattery;
    private TextView tvPercent;
    private ProgressBar pbBattery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_update);

        tvPercent = findViewById(R.id.tv_battery_update_percent);
        tvPercent.setText("50");
        bvBattery = findViewById(R.id.bv_battery_update_value);

        pbBattery = findViewById(R.id.pb_act_battery_update_value);
        pbBattery.setProgress(50);

        EditText etBatteryValue = findViewById(R.id.et_act_battery_update_value);
        Button btnUpdate = findViewById(R.id.btn_battery_update);
        btnUpdate.setOnClickListener(v -> {
            String batteryStr = etBatteryValue.getText().toString();
            tvPercent.setText(batteryStr);
            int power = 0;
            if (! TextUtils.isEmpty(batteryStr)) {
                power = Integer.parseInt(batteryStr);
            }
            bvBattery.setPower(power);
            updateProgress(power);
        });

        updateThread();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_UPDATE_VIEW) {
                int power = (int) msg.obj;
                bvBattery.setPower(power);
                tvPercent.setText(String.valueOf(power));
                updateProgress(power);
            }
        }
    };

    private void updateProgress(int power) {
        if (power <= 20) {
            pbBattery.setProgressDrawable(getResources().getDrawable(R.drawable.bg_pb_20_battery));
        } else if (power <= 40){
            pbBattery.setProgressDrawable(getResources().getDrawable(R.drawable.bg_pb_40_battery));
        } else if (power <= 60) {
            pbBattery.setProgressDrawable(getResources().getDrawable(R.drawable.bg_pb_60_battery));
        } else {
            pbBattery.setProgressDrawable(getResources().getDrawable(R.drawable.bg_pb_100_battery));
        }
        pbBattery.setProgress(power);
    }

    // 定时100s，1s更新一次电量
    private void updateThread() {
        new Thread(this::update).start();
    }

    private void update() {
        int count = 0;
        while(count <= 100) {
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mHandler.obtainMessage(MSG_UPDATE_VIEW, count).sendToTarget();
            count++;
        }
    }
}
