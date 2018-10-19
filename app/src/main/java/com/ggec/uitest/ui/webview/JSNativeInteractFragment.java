package com.ggec.uitest.ui.webview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.ggec.uitest.R;

/**
 * Created by ggec on 2018/10/18.
 * JS与Native交互，包括JS调用Native的方法、Native调用JS里面的方法、Java得到JS中的返回值
 */

public class JSNativeInteractFragment extends Fragment {
    private static final String TAG = "JSNativeInteractFrag";

    private WebView webView;

    @Nullable
    @Override
    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_js_native_interact, container, false);
        Button btnStart = view.findViewById(R.id.btn_js_native_interact_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("file:///android_asset/web.html");
//                webView.loadUrl("http://www.w3school.com.cn/");
            }
        });
        Button btnJsInvokeNative = view.findViewById(R.id.btn_js_native_interact_native_invoke_js);
        btnJsInvokeNative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 注意调用的JS方法名要对应上调用javascript的native2js()方法
                webView.loadUrl("javascript:native2js(3,8)");
            }
        });

        webView = view.findViewById(R.id.wv_js_native_interact);

        WebSettings mWebSettings =  webView.getSettings();
        // 在WebView中启用JavaScript
        mWebSettings.setJavaScriptEnabled(true);
        // 让android能读取js的节点
        mWebSettings.setDomStorageEnabled(true);

        // 将对象注入到WebView中，在WebView中的对象别名叫test
        webView.addJavascriptInterface(new AndroidToJs(getContext()), "test");

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//                Log.e(TAG,"xxx:message = " + message);
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                result.confirm(); // 去掉后，再次点击alert按钮会失效
                return true;    // 返回false(默认)的话，系统还会弹出Alert对话框
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                result.confirm(); // 去掉后，再次点击alert按钮会失效
                return true;    // 返回false(默认)的话，系统还会弹出Confirm对话框
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                Log.e(TAG,"onJsPrompt(),message = " + message + ",defaultValue = " + defaultValue);
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                result.confirm(); // 去掉后，再次点击alert按钮会失效
                return false;    // 返回false(默认)的话，系统还会弹出Prompt对话框
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Toast.makeText(getContext(), consoleMessage.message(),Toast.LENGTH_SHORT).show();
                return true;   // 返回false(默认)的话，在控制台可以看到消息日志
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                showJsResult();
            }
        });

        return view;
    }

    // 一定要在Html加载完毕后调用，否则会异常：Uncaught ReferenceError
    private void showJsResult() {
        webView.evaluateJavascript("getBackResult()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                Toast.makeText(getContext(), "从JS中返回的值为：" + value, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
