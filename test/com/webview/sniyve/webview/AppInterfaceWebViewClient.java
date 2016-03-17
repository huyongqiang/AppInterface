package com.webview.sniyve.webview;

import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sniyve.ai.AppInterface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yanglang on 16/3/13.
 */
public class AppInterfaceWebViewClient extends WebViewClient {

    private WebView webView;

    public AppInterfaceWebViewClient(WebView webView){
        this.webView = webView;
    }

    public AppInterfaceWebViewClient(){

    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.v("warning",url);
        boolean flag = AppInterface.getInstance().handle(view,url);
        return flag?flag:super.shouldOverrideUrlLoading(view, url);

    }

    private JSONObject packageData(boolean success, String message, JSONObject data) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("success",success);
        json.put("code",success?0:1);
        json.put("message",message);
        json.put("data",data);
        return json;
    }
}
