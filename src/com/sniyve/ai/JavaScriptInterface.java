package com.sniyve.ai;

import android.content.Context;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import org.json.JSONException;

/**
 * Created by yanglang on 16/3/14.
 * Javascript接口类
 */

public class JavaScriptInterface {
    private Handler handler = new Handler();

    WebView webView;

    public JavaScriptInterface(WebView webView) {
        this.webView = webView;
    }

    @JavascriptInterface
    public void call(final String url){
        //调用UI线程处理拦截业务
        handler.post(new Runnable() {
            @Override
            public void run() {
                AppInterface.getInstance().handle(webView, url, true);
            }
        });
    }

    public WebView getWebView() {
        return webView;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }
}
