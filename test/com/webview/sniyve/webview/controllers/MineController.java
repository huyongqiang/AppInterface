package com.webview.sniyve.webview.controllers;

import android.content.Context;
import android.content.Intent;

import com.sniyve.ai.AppInterface;
import com.sniyve.ai.AppInterfaceCallback;
import com.sniyve.ai.BaseController;
import com.sniyve.ai.annotation.Controller;
import com.sniyve.ai.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

import demo.sniyve.com.webview.Main2Activity;

/**
 * Created by yanglang on 2016/3/17.
 */
@Controller("mine")
public class MineController extends AnotherCommonController {

    @RequestMapping("/order_manage")
    public void orderManage(Map<String,Object> params, final AppInterfaceCallback callback){
        Map<String,Object> pms = new HashMap<String,Object>();
        pms.put("value", params.get("shopId"));
        AppInterface.getInstance().notify("onClick",pms);
        Intent intent = new Intent(this.getContext(), Main2Activity.class);
        intent.setAction("fds");
        this.getContext().startActivity(intent);
    }
}

