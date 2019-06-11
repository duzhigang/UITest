package com.ggec.uitest.ui.edittext;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.ggec.uitest.R;

/**
 * Created by ggec on 2018/8/30.
 * 自定义EditText，主要包括EditText下划线、光标以及软盘的处理
 */

public class ETFragment extends Fragment {
    private static final String TAG = "ETFragment";

    private Button btnConfirm;
    private EditText etName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final String strModify = "修改";
        final String strConfirm = "确认";
        String content = "自定义名字";
        View view = inflater.inflate(R.layout.fragment_edit_text, container, false);
        etName = view.findViewById(R.id.et_name);
        etName.setText(content);
        etName.setSelection(content.length());
        etName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Log.i(TAG,"获取EditText焦点");
                btnConfirm.setText(strConfirm);
                showSoftInputFromWindow(etName);
            }
        });
        etName.setOnClickListener(v -> {
            Log.i(TAG,"选中EditText");
            btnConfirm.setText(strConfirm);
            showSoftInputFromWindow(etName);
        });

        btnConfirm = view.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(v -> {
            if (btnConfirm.getText().equals(strConfirm)) {
                Log.i(TAG,"点击了确认");
                btnConfirm.setText(strModify);
                hideSoftInputFromWindow(etName);
            } else {
                Log.i(TAG,"点击了修改");
                btnConfirm.setText(strConfirm);
                showSoftInputFromWindow(etName);
            }
        });
        return view;
    }

    public void showSoftInputFromWindow(EditText editText) {
        editText.setCursorVisible(true);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText,0);
    }

    public void hideSoftInputFromWindow(EditText editText) {
        editText.setCursorVisible(false);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
    }
}
