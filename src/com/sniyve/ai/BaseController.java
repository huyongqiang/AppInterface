package com.sniyve.ai;

import android.content.Context;

/**
 * Created by yanglang on 16/3/13.
 * 基础控制器，所有控制器需要继承自它
 * 提供了getContext方法获取Activity上下文对象
 */
public abstract class BaseController {
    
    private Context context;

      public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
