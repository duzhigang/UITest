package com.ggec.uitest.ui.dialog;

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
import android.widget.TextView;

import com.ggec.uitest.R;


/**
 * Created by ggec on 2018/11/8.
 * 在Dialog中添加ProgressBar旋转菊花加载的动画
 * https://blog.csdn.net/zhangphil/article/details/79453384(采用的是这种方法)
 * https://blog.csdn.net/qq_21376985/article/details/52847317
 */

public class WaitDialog extends DialogFragment {
    private static final String TAG = "WaitDialog";

    private String content = "";

    public static WaitDialog newInstance(String content) {
        WaitDialog dialog = new WaitDialog();
        Bundle args = new Bundle();
        args.putString("content", content);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        if (window == null) return;
        FragmentActivity activity = getActivity();
        if (activity == null) return;
        WindowManager.LayoutParams params = window.getAttributes();
        float scale = activity.getApplication().getResources().getDisplayMetrics().density;
        params.width = (int) (150 * scale + 0.5f);  // dp2px
        params.dimAmount = 0.5f;
        window.setAttributes(params);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        // dialog弹出后，点击屏幕或物理返回键，dialog不消失
        setCancelable(false);

        String defaultContent = "提示";
        if (getArguments() != null) {
            this.content = getArguments().getString("content", defaultContent);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_wait, container);
//        ProgressBar pbWait = (ProgressBar) view.findViewById(R.id.pb_wait_dialog_loading);
        TextView tvContent = view.findViewById(R.id.tv_wait_dialog_content);
        tvContent.setText(content);
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

    public void dismissDialog() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            super.dismissAllowingStateLoss();
        }
    }
}
