package com.ggec.uitest.ui.seekbar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ggec.uitest.R;

public class SKFrameFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sk_frame, container, false);
        Button btnStart = view.findViewById(R.id.btn_frag_sk_frame_start);
        btnStart.setOnClickListener(v -> {
            FragmentManager fm = getFragmentManager();
            VolumeBarFragment fragment = new VolumeBarFragment();
            fragment.show(fm, "SeekBarDialog");
        });
        return view;
    }
}
