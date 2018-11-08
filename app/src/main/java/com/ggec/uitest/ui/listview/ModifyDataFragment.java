package com.ggec.uitest.ui.listview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ggec.uitest.R;

/**
 * Created by ggec on 2018/8/31.
 */

public class ModifyDataFragment extends Fragment {
    private static final String TAG = "ModifyDataFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modify_data, container, false);
        final EditText etName = (EditText) view.findViewById(R.id.et_modify_data);
        Button btnSend = (Button) view.findViewById(R.id.btn_modify_data);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = etName.getText().toString().trim();
                Intent intent = new Intent();
                intent.putExtra("result", str);
                Log.e(TAG,"发送结果为：" + str);
                getTargetFragment().onActivityResult(getTargetRequestCode(), 2, intent);
                getFragmentManager().popBackStack();
            }
        });
        return view;
    }
}
