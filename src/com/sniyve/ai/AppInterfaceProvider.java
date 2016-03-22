package com.sniyve.ai;

import android.webkit.WebView;

import org.json.JSONObject;

/**
 * Created by yanglang on 16/3/13.
 * 调用js回调
 */
public abstract class AppInterfaceProvider {

    public void callback(boolean success, String message, JSONObject result){};
    public void callback(JSONObject result){};
    public abstract WebView getWebView();
}
