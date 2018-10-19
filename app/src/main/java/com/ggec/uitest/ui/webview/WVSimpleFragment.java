package com.ggec.uitest.ui.webview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.ggec.uitest.R;

/**
 * Created by ggec on 2018/10/17.
 * 只测试显示一个网页并且支持js
 */

public class WVSimpleFragment extends Fragment {
    private static final String TAG = "WVSimpleFrag";

    private static final String url = "http://www.baidu.com/";
    private WebView webView;

    @Nullable
    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreateView()");
        View view = inflater.inflate(R.layout.fragment_wv_simple, container, false);
        ImageButton imgBtnBack = view.findViewById(R.id.imgbtn_wv_simple_back);
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        webView = view.findViewById(R.id.wv_simple_webview);
        WebSettings mWebSettings = webView.getSettings();
        // 设置自适应屏幕，两者合用
        mWebSettings.setUseWideViewPort(true);  // 调整到适合webview的大小
        mWebSettings.setLoadWithOverviewMode(true);  // 缩放至屏幕的大小
        mWebSettings.setJavaScriptEnabled(true);
        // 缩放操作
        mWebSettings.setSupportZoom(true);  // 支持缩放，默认为true,是下面那个的前提。
        mWebSettings.setBuiltInZoomControls(true);  // 设置内置的缩放控件。若为false，则该WebView不可缩放
        mWebSettings.setDisplayZoomControls(false);  // 隐藏原生的缩放控件

        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient());

        return view;
    }

    @Override
    public void onDestroy() {
        Log.v(TAG,"onDestroy()");
        super.onDestroy();

        //释放资源
        webView.destroy();
        webView=null;
    }
}
