package com.ggec.uitest.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ggec.uitest.R;
import com.ggec.uitest.utils.ScreenUtil;


/**
 * Created by ggec on 2018/4/3.
 * 普通的包含：标题、内容、取消、确认对话框
 * DialogFragment中在LinearLayout中设置宽度和高度无效
 */

public class MyAlertDialog extends DialogFragment {
    private static final String TAG = "MyNormalDialog";
    private static final float WIDTH_PERCENT = 0.8f;

    private Callback callback;
    private String title = "";
    private String content = "";
    private String negativeName = "";
    private String positiveName = "";
    private boolean leftBtnExit = true;
    private boolean rightBtnExit = true;

    public static MyAlertDialog newInstance(String title, String content, String negativeString, boolean leftBtnExist, String positiveString, boolean rightBtnExist) {
        MyAlertDialog fragment = new MyAlertDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("content", content);
        args.putString("negativeString", negativeString);
        args.putBoolean("leftBtnExit", leftBtnExist);
        args.putString("positiveString", positiveString);
        args.putBoolean("rightBtnExist", rightBtnExist);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.v(TAG,"onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        if (window == null) return;
        FragmentActivity activity = getActivity();
        if (activity == null) return;
        WindowManager.LayoutParams params = window.getAttributes();
        // 设置Dialog的Width
        params.width = (int) (ScreenUtil.getScreenWidth(activity) * WIDTH_PERCENT);
        // 设置窗体本身透明度 0.0f~1.0f(完全透明~完全不透明)
//        params.alpha = 0.5f;
        // 设置背景黑暗度 0.0f~1.0f(完全不暗~完全暗),设置了之后style对应的预先设置值无效
        params.dimAmount = 0.5f;
        window.setAttributes(params);
    }

    @Override
    public void onAttach(Context context) {
        Log.v(TAG,"onAttach()");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreate()");
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
//        setStyle(DialogFragment.STYLE_NO_FRAME, 0); //NO_FRAME就是dialog无边框，0指的是默认系统Theme

        String defaultContent = "";
        String defaultNegativeString = "取消";
        String defaultPositiveString = "确定";
        if (getArguments() != null) {
            this.title = getArguments().getString("title", "提示");
            this.content = getArguments().getString("content", defaultContent);
            this.negativeName = getArguments().getString("negativeString", defaultNegativeString);
            this.positiveName = getArguments().getString("positiveString", defaultPositiveString);
            this.leftBtnExit = getArguments().getBoolean("leftBtnExit", true);
            this.rightBtnExit = getArguments().getBoolean("rightBtnExit", true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreateView()");
        View view = inflater.inflate(R.layout.dialog_my_alert, container);
        TextView tvTitle = view.findViewById(R.id.tv_dialog_my_alert_title);
        tvTitle.setText(title);
        TextView tvContent = view.findViewById(R.id.tv_dialog_my_alert_content);
        tvContent.setText(content);
        Button btnNegative = view.findViewById(R.id.btn_dialog_my_alert_nag);
        if (leftBtnExit) {
            btnNegative.setVisibility(View.VISIBLE);
            btnNegative.setBackgroundResource(R.drawable.selector_btn_border_bg);
        } else {
            btnNegative.setVisibility(View.GONE);
        }
        btnNegative.setText(negativeName);
        btnNegative.setOnClickListener(v -> {
            dismiss();
            callback.callback(0);
        });

        Button btnPositive = view.findViewById(R.id.btn_dialog_my_alert_pos);
        if (rightBtnExit) {
            btnPositive.setVisibility(View.VISIBLE);
            btnPositive.setBackgroundResource(R.drawable.selector_btn_border_bg);
        } else {
            btnPositive.setVisibility(View.GONE);
        }
        btnPositive.setText(positiveName);
        btnPositive.setOnClickListener(v -> {
            dismiss();
            callback.callback(1);
        });

        return view;
    }

    // 重写该方法是为了避免出现以下崩溃：
    // IllegalStateException : Can not perform this action after onSaveInstanceSate
    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (IllegalStateException ignore) {
//            e.printStackTrace();
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

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
