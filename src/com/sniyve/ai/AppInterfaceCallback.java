package com.sniyve.ai;

import org.json.JSONObject;

/**
 * Created by yanglang on 16/3/13.
 */
public abstract class AppInterfaceCallback {

    public void call(boolean success,String message,JSONObject result){};
    public void call(JSONObject result){};

}
