package com.sniyve.ai;

import android.webkit.WebView;

import org.json.JSONObject;

/**
 * Created by yanglang on 16/3/13.
 * 提供器，提供回调接口，当前WebView对象以及将来会用到的东西
 */
public abstract class AppInterfaceProvider {

    public void callback(boolean success, String message, JSONObject result){};
    public void callback(JSONObject result){};
    public abstract WebView getWebView();
}
