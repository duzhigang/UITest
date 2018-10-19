package com.ggec.uitest.ui.webview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ggec.uitest.R;

/**
 * Created by ggec on 2018/10/17.
 */

public class WVTestFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wv_test, container, false);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        Button btnSimple = view.findViewById(R.id.btn_wv_test_simple);
        btnSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new WVSimpleFragment();
                ft.addToBackStack(null);
                ft.replace(R.id.webview_frame, fragment).commit();
            }
        });

        Button btnNormal = view.findViewById(R.id.btn_wv_test_normal);
        btnNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WVNormalActivity.class);
                getActivity().startActivity(intent);
            }
        });

        Button btnInteract = view.findViewById(R.id.btn_wv_js_natvie);
        btnInteract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new JSNativeInteractFragment();
                ft.addToBackStack(null);
                ft.replace(R.id.webview_frame, fragment).commit();
            }
        });
        return view;
    }
}
