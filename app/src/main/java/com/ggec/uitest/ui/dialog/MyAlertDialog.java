package com.ggec.uitest.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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


/**
 * Created by ggec on 2018/4/3.
 * 普通的包含：标题、内容、取消、确认对话框
 * DialogFragment中在LinearLayout中设置宽度和高度无效
 */

public class MyAlertDialog extends DialogFragment {
    private static final String TAG = "MyNormalDialog";

    private Callback callback;
    private String title = "";
    private String content = "";
    private String negativeName = "";
    private String positiveName = "";

    public static MyAlertDialog newInstance(String title, String content, String negativeString, String positiveString) {
        MyAlertDialog fragment = new MyAlertDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("content", content);
        args.putString("negativeString", negativeString);
        args.putString("positiveString", positiveString);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            float scale = getActivity().getApplication().getResources().getDisplayMetrics().density;
            params.width = (int) (270 * scale + 0.5f);  // dp2px
            getDialog().getWindow().setAttributes(params);
        }
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
        btnNegative.setText(negativeName);
        btnNegative.setOnClickListener(v -> {
            dismiss();
            callback.callback(0);
        });
        Button btnPositive = view.findViewById(R.id.btn_dialog_my_alert_pos);
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
