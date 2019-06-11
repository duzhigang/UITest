package com.ggec.uitest.ui.wifi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.ggec.uitest.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ggec on 2018/11/7.
 * 802.1x网络配置，Spinner通过spinner.setSelection(pos,true)指定显示的Item
 * 初始化Spinner时一定会执行一次onItemSelected()方法
 */

public class WPAEnterpriseActivity extends FragmentActivity {
    private static final String TAG = "WPAEnpActivity";

    private List<String> phase2List = new ArrayList<>();
    private String eapMethod = "";
    private String phase2 = "";
    private String identity = "";
    private ArrayAdapter<String> phase2Adapter;
    private View viewPhase2;
    private Spinner spinnerPhase2;
    private View viewPwd;
    private EditText etPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wpa_enterprise);
        String[] phase2s = getResources().getStringArray(R.array.wap_enp_activity_peap_phase2);
        Collections.addAll(phase2List, phase2s);
        Spinner spinnerEapMethod = findViewById(R.id.sp_wpa_enp_eap_method);
        spinnerEapMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] eapMethods = getResources().getStringArray(R.array.wap_enp_activity_eap_methods);
                eapMethod = eapMethods[position];
                if ("PEAP".equals(eapMethod)) {
                    viewPhase2.setVisibility(View.VISIBLE);
                    String [] phase2s = getResources().getStringArray(R.array.wap_enp_activity_peap_phase2);
                    phase2List.clear();
                    Collections.addAll(phase2List, phase2s);
                    phase2Adapter.notifyDataSetChanged();
                    // 设置默认选中项
                    spinnerPhase2.setSelection(0, true);
                    viewPwd.setVisibility(View.VISIBLE);
                } else if ("TLS".equals(eapMethod)){
                    viewPhase2.setVisibility(View.GONE);
                    viewPwd.setVisibility(View.GONE);
                    // 情况输入框里面的内容
                    etPwd.getText().clear();
                } else if ("TTLS".equals(eapMethod)) {
                    viewPhase2.setVisibility(View.VISIBLE);
                    String [] phase2s = getResources().getStringArray(R.array.wap_enp_activity_ttls_phase2);
                    phase2List.clear();
                    Collections.addAll(phase2List, phase2s);
                    phase2Adapter.notifyDataSetChanged();
                    spinnerPhase2.setSelection(0, true);
                    viewPwd.setVisibility(View.VISIBLE);
                } else if ("PWD".equals(eapMethod)) {
                    viewPhase2.setVisibility(View.GONE);
                    viewPwd.setVisibility(View.VISIBLE);
                }
                Log.v(TAG,"spinnerEapMethod选中：" + eapMethod);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.v(TAG,"spinnerEapMethod默认：");
            }
        });

        viewPhase2 = findViewById(R.id.ll_wap_enp_phase2);
        spinnerPhase2 = findViewById(R.id.sp_wpa_enp_phase2);
        phase2Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, phase2List);
        phase2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhase2.setAdapter(phase2Adapter);
        spinnerPhase2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                phase2 = phase2List.get(position);
                Log.v(TAG,"spinnerPhase2选中：" + phase2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.v(TAG,"spinnerPhase2默认：");
            }
        });

        final EditText etIdentity = findViewById(R.id.et_wpa_enp_identity);
        viewPwd = findViewById(R.id.ll_wap_enp_pwd);
        etPwd = findViewById(R.id.et_wpa_enp_pwd);

        Button btnConnect = findViewById(R.id.btn_wpa_enp_connect);
        btnConnect.setOnClickListener(v -> {
            identity = etIdentity.getText().toString().trim();
            String pwd = etPwd.getText().toString().trim();
            if (viewPhase2.getVisibility() == View.GONE) {
                phase2 = "";
            }
            Log.e(TAG,"eapMethod = " + eapMethod + ",phase2 = " + phase2 + ",identity = " + identity + ",pwd = " + pwd);
        });
    }
}
