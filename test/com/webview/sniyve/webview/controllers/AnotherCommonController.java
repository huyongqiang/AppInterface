package com.webview.sniyve.webview.controllers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.sniyve.ai.AppInterfaceCallback;
import com.sniyve.ai.BaseController;
import com.sniyve.ai.annotation.Controller;
import com.sniyve.ai.annotation.RequestMapping;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by yanglang on 16/3/14.
 */
@Controller("common")
public class AnotherCommonController extends BaseController {

    public AnotherCommonController(Context context) {
        super(context);
    }

    @RequestMapping("/login")
    public void login(Map<String,Object> params, final AppInterfaceCallback callback){
        Log.v("error", "come in login");
        try{
            callback.call(true,"进入登录~",new JSONObject("{}"));

        }catch (Exception e){}
    }

    @RequestMapping("/aaa/bbb/ccc")
    public void abc(Map<String,Object> params, final AppInterfaceCallback callback){
        Log.v("error", "come in aaabbbccc");
        try{
            callback.call(true,"进入aaabbbccc，可以想配啥地址配啥地址，不再需要判断了。",new JSONObject("{}"));
        }catch (Exception e){}
    }
}
