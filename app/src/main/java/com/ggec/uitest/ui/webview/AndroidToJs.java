package com.ggec.uitest.ui.webview;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by ggec on 2018/10/18.
 * 为了解决addJavascriptInterface()函数的安全问题，在android:targetSdkVersion数值为17（Android4.2）
 * 及以上的APP中，JS只能访问带有 @JavascriptInterface注解的Java函数，所以如果你的android:targetSdkVersion是17+，
 * 与JS交互的Native函数中，必须添加JavascriptInterface注解，不然无效
 */

public class AndroidToJs {
//    private Context context = MyApplication.getMyApplication();
    private Context context;

    public AndroidToJs(Context context) {
        this.context = context;
    }

    // 被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    public void hello(String msg) {
        Toast.makeText(context,"通过JS传递给Native的消息：" + msg, Toast.LENGTH_LONG).show();
    }
}
