package com.ggec.uitest.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.ggec.uitest.R;

/**
 * Created by ggec on 2018/8/31.
 * 测试DialogFragment相关内容
 */

public class FragmentDialogActivity extends FragmentActivity {
    private static final String TAG = "FragmentDialogActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_fragment);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DialogLifeCycleFragment fragment = new DialogLifeCycleFragment();
        ft.replace(R.id.dialog_fragment_frame, fragment).commit();
    }
}
