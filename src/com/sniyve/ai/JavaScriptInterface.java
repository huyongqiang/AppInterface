package com.sniyve.ai;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

/**
 * Created by yanglang on 16/3/14.
 * Javascript接口类
 */

public class JavaScriptInterface {
    Context context;

    WebView webView;

    public JavaScriptInterface(Context context,WebView webView) {
        this.context = context;
        this.webView = webView;
    }

    @JavascriptInterface
    public void call(String url){
        AppInterface.getInstance().handle(webView,url,true);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public WebView getWebView() {
        return webView;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }
}
