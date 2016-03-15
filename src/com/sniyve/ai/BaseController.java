package com.sniyve.ai;

import android.content.Context;

/**
 * Created by yanglang on 16/3/13.
 */
public abstract class BaseController {
    
    private Context context;

    public BaseController(Context context){
        this.context = context;
    };

    public BaseController(){}

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
