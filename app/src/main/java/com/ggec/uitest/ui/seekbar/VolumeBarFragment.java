package com.ggec.uitest.ui.seekbar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ggec.uitest.R;

public class VolumeBarFragment extends DialogFragment {
    private static final String TAG = "VolumeBarFragment";

    private Device device;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_volume_bar, container, false);
        device = ((SeekBarActivity) getActivity()).getDevice();
        TextView tvVolume = view.findViewById(R.id.tv_frag_seekbar_volume);
        tvVolume.setText(device.getName());
        SeekBar skVolume = view.findViewById(R.id.sk_frag_seekbar_volume);
        int volume = device.getVolume();
        Log.e(TAG,"volume = " + volume);
        skVolume.setMax(100);
        skVolume.setProgress(volume);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setLayout(width * 5 / 6, height * 3 / 4);
        }
    }
}
