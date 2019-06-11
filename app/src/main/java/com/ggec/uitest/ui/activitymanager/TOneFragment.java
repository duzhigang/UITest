package com.ggec.uitest.ui.activitymanager;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ggec.uitest.R;

/**
 * Created by ggec on 2018/10/26.
 */

public class TOneFragment extends Fragment {
    private static final String TAG = "TOneFragment";

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
        View view = inflater.inflate(R.layout.activity_a, container, false);
        TextView tvTitle = view.findViewById(R.id.tv_activity_a_title);
        tvTitle.setText("TOne Fragment");
        Button btnStart = view.findViewById(R.id.btn_activity_a_start);
        btnStart.setOnClickListener(v -> {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            TTwoFragment fragment = new TTwoFragment();
            ft.addToBackStack(null);
            ft.replace(R.id.activity_t_frame, fragment).commit();
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
