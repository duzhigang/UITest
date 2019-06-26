package com.ggec.uitest.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ggec.uitest.R;

/**
 * Created by ggec on 2018/7/31.
 * DialogLifeCycleFragment和MyNormalDialog是为了测试DialogFragment的打开与结束对上一个Fragment生命周期的影响
 * 结果：DialogFragment对上一层Fragment的生命周期没有任何影响。
 */

public class DialogLifeCycleFragment extends Fragment{
    private static final String TAG = "DialogLifeCycleFragment";

    @Override
    public void onAttach(Context context) {
        Log.v(TAG,"onAttach()");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreate()");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreateView()");
        View view = inflater.inflate(R.layout.fragment_dialog_life_cycle, container,false);

        Button btnStartOneAlertDialog = view.findViewById(R.id.btn_dialog_life_cycle_start_one_alert_dialog);
        btnStartOneAlertDialog.setOnClickListener(v -> {
            Log.i(TAG,"启动只有确定按钮的AlertDialog");
            String content = "测试只有确定按钮的AlertDialog";
            MyAlertDialog dialog = MyAlertDialog.newInstance(null,content,null, false, null, true);
            dialog.setCallback(position -> {
                switch (position) {
                    case 0:
                        Log.i(TAG,"点击取消");
                        break;
                    case 1:
                        Log.i(TAG,"点击确定");
                        break;
                }
            });
            dialog.show(getFragmentManager(), "one_alert_dialog");
        });

        Button btnStarTwoAlertDialog = view.findViewById(R.id.btn_dialog_life_cycle_start_two_alert_dialog);
        btnStarTwoAlertDialog.setOnClickListener(v -> {
            Log.i(TAG,"启动包含确定和取消的AlertDialog");
            String content = "测试包含确定和取消的AlertDialog";
            MyAlertDialog dialog = MyAlertDialog.newInstance(null,content,null, true, null, true);
            dialog.setCallback(position -> {
                switch (position) {
                    case 0:
                        Log.i(TAG,"点击取消");
                        break;
                    case 1:
                        Log.i(TAG,"点击确定");
                        break;
                }
            });
            dialog.show(getFragmentManager(), "two_alert_dialog");
        });

        Button btnStartWaitDialog = view.findViewById(R.id.btn_dialog_life_cycle_start_wait_dialog);
        btnStartWaitDialog.setOnClickListener(v -> {
            Log.i(TAG,"启动WaitDialog");
            WaitDialog dialog = WaitDialog.newInstance("正在启动，请稍后...");
            dialog.show(getFragmentManager(), "test_wait_dialog");
            new Handler().postDelayed(dialog::dismissDialog, 5000);
        });
        return view;
    }

    @Override
    public void onStart() {
        Log.v(TAG,"onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.v(TAG,"onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.v(TAG,"onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.v(TAG,"onStop()");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.v(TAG,"onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.v(TAG,"onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.v(TAG,"onDetach()");
        super.onDetach();
    }
}
