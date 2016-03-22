package com.webview.sniyve.webview.controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.sniyve.ai.AppInterface;
import com.sniyve.ai.AppInterfaceProvider;
import com.sniyve.ai.BaseController;
import com.sniyve.ai.annotation.Controller;
import com.sniyve.ai.annotation.RequestMapping;
import com.webview.sniyve.webview.Main2Activity;

import org.json.JSONObject;

import java.util.Map;


/**
 * Created by yanglang on 16/3/13.
 */
@Controller("common")
public class CommonController extends BaseController{

    @RequestMapping("/toast")
    public void toast(Map<String,Object> params,AppInterfaceProvider provider){
        String message = (String)params.get("msg");
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
        provider.callback(null);
    }

    @RequestMapping("/confirm")
    public void confirm(Map<String,Object> params, final AppInterfaceProvider provider){
        String message = (String)params.get("msg");
        String title = (String)params.get("title");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        try{
                            provider.callback(new JSONObject("{isCancel:false}"));
                        }catch (Exception e){}
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try{
                            provider.callback(new JSONObject("{isCancel:true}"));
                        }catch (Exception e){}
                    }
                });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });

        // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
                Log.v("onJsConfirm", "keyCode==" + keyCode + "event=" + event);
                return true;
            }
        });
        // 禁止响应按back键的事件
        // builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @RequestMapping("/logout")
    public void logout(Map<String,Object> params, final AppInterfaceProvider provider){
        Log.v("error", "come in logout");
        try{
            provider.callback(new JSONObject("{}"));
        }catch (Exception e){}
    }

    @RequestMapping("/localJump")
    public void localJump(Map<String,Object> params,AppInterfaceProvider provider){
        String message = (String)params.get("url");
        Intent intent = new Intent(this.getContext(), Main2Activity.class);
        intent.putExtra("url", message);
        this.getContext().startActivity(intent);

        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
        AppInterface.getInstance().callH5(provider.getWebView(),"onLocalJump");
        provider.callback(null);
    }
}
