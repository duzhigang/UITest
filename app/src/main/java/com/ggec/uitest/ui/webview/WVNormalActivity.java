package com.ggec.uitest.ui.webview;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.ggec.uitest.R;

/**
 * Created by ggec on 2018/10/18.
 * 正常的处理WebView页面及在网页内部的返回处理
 * 包括页面的缩放、页面标题、加载开始、加载结束、加载进度等
 */

public class WVNormalActivity extends FragmentActivity {
    private static final String TAG = "WVNormalActivity";

    private WebView  webView;
    private TextView tvTitle, tvStatus;

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wv_normal);
        tvTitle =  findViewById(R.id.wv_normal_title);
        tvStatus = findViewById(R.id.wv_normal_status);
        webView = findViewById(R.id.wv_normal_webview);

        WebSettings mWebSettings =  webView.getSettings();
        // 在WebView中启用JavaScript
        mWebSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置自适应屏幕，两者合用
        // 注意：对于我们自己写的网页代码，不必利用这处函数来做页面缩放适配，因为对于有些手机存在适配问题，只需要在HTML中做宽度100%自适应屏幕就行了
        mWebSettings.setUseWideViewPort(true);  // 调整到适合webview的大小
        mWebSettings.setLoadWithOverviewMode(true);  // 缩放至屏幕的大小
        // 缩放操作
        mWebSettings.setSupportZoom(true);  // 支持缩放，默认为true,是下面那个的前提。
        mWebSettings.setBuiltInZoomControls(true);  // 设置内置的缩放控件。若为false，则该WebView不可缩放
        mWebSettings.setDisplayZoomControls(false);  // 隐藏原生的缩放控件
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS); // 使所有列的宽度不超过屏幕宽度
        // 如果webView中需要用户手动输入用户名、密码或其他，则WebView必须设置支持获取手势焦点
         webView.requestFocusFromTouch();

        webView.loadUrl("file:///android_asset/web.html");
//         webView.loadUrl("http://www.baidu.com/");
        //设置WebChromeClient类
         webView.setWebChromeClient(new WebChromeClient() {
            //获取网站标题，这个方法的回掉次数不固定，并且获取到的title有时候为url
            @Override
            public void onReceivedTitle(WebView view, String title) {
                Log.v(TAG,"onReceivedTitle(),title = " + title);
                tvTitle.setText(title);
            }

            //获取加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                String progress = newProgress + "%";
                tvStatus.setText(getString(R.string.wv_normal_fragment_load_status, progress));
            }
        });

//         webView.setWebViewClient(new WebViewClient()); //增加这一行可以指定在WebView打开链接
        //设置WebViewClient类
         webView.setWebViewClient(new WebViewClient() {
            //设置加载前的函数
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.v(TAG,"onPageStarted(),url = " + url);
                tvStatus.setText(getString(R.string.wv_normal_fragment_load_status, "开始加载"));
            }

            //设置结束加载函数
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.v(TAG,"onPageFinished(),url = " + url);
                tvStatus.setText(getString(R.string.wv_normal_fragment_load_status, "加载完成"));

            }

            // 设置在APP内部加载网页的方式
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // 默认返回false由WebView处理
                // 返回true则会屏蔽系统默认的显示URL结果的行为，不需要处理的URL也需要调用loadUrl()来加载进WebVIew，
                // 不然就会出现白屏，所以般建议大家return false，我们只关心我们关心的拦截内容，对于不拦截的内容，让系统自己来处理即可。
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
    }

    // 点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.v(TAG,"onKeyDown()");
        if (keyCode == KeyEvent.KEYCODE_BACK &&  webView.canGoBack()) {
            Log.v(TAG,"自己处理返回");
             webView.goBack();
            return true;
        }
        Log.v(TAG,"系统处理返回");
        return super.onKeyDown(keyCode, event);
    }

    // 销毁WebView
    @Override
    protected void onDestroy() {
        Log.v(TAG,"onDestroy()");
        if ( webView != null) {
             webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
             webView.clearHistory();

            ((ViewGroup)  webView.getParent()).removeView( webView);
             webView.destroy();
             webView = null;
        }
        super.onDestroy();
    }

}
