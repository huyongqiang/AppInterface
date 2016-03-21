package com.sniyve.ai;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;

import com.sniyve.ai.annotation.Controller;
import com.sniyve.ai.annotation.RequestMapping;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yanglang on 16/3/13.
 * AppInterface 核心处理
 */
public class AppInterface {

    private static AppInterface appInterface;

    private Map<String,List<Callback>> events = new HashMap<String, List<Callback>>();

    private Context context;

    private Handler handler = new Handler();

    private List<MappingHost> mappingHostList = new ArrayList<MappingHost>();

    private AppInterface(){};

    public static AppInterface getInstance(){
        if(appInterface == null)
            appInterface = new AppInterface();
        return appInterface;
    }

    /**
     * 初始化入口
     * @param context
     * @param controllerPath
     */
    public AppInterface init(Context context,String controllerPath){
        this.context = context;
        try {
            Date startTime = new Date();
            this.doInit(controllerPath);
            Log.v("info", "AppInterface框架初始总耗时:"+String.valueOf(new Date().getTime()-startTime.getTime())+"ms");
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * 初始化js桥处理功能
     * @param webView
     * @return
     */
    public AppInterface initJsBridge(WebView webView){
        webView.addJavascriptInterface(new JavaScriptInterface(webView),"ApplicationInterface");
        return this;
    }

    /**
     * 初始化环境，利用反射，缓存所有Controller对象
     * @param controllerPath Controller包路径
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    private AppInterface doInit(String controllerPath) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        List<Class<?>> list = Scanner.scan(context,controllerPath);
        for (Class<?> controllerClass : list) {
            //controllerClass 类名
            Controller controller = controllerClass
                    .getAnnotation(Controller.class);
            if (controller != null) {
                String host = controller.value();
                String scheme = controller.scheme();

                MappingHost mappingHost = findMappingBySchemeAndHost(scheme, host, true);

                //判断实例是否已在缓存池中
                String className = controllerClass.getName();
                if(!InstancePool.has(className)){
                    Constructor c1 = controllerClass.getDeclaredConstructor();
                    c1.setAccessible(true);
                    Object owner = owner = (Object)c1.newInstance();
                    //获取父类方法BaseController的setContext注入context对象
                    Method method = controllerClass.getMethod("setContext",Context.class);
                    method.invoke(owner,new Object[]{this.context});
                    InstancePool.setInstance(className, owner);
                }
                Method[] methods = controllerClass.getMethods();
                for(Method method : methods){
                    RequestMapping tmp = method.getAnnotation(RequestMapping.class);
                    if(tmp!=null){
                        mappingHost.addMappingPath(new MappingPath(tmp.value(),className, method));
                    }
                }
            }else{
                Log.v("warn","no Controller controller "+controllerClass.getName());
            }
        }
        return this;
    }

    /**
     * 根据scheme与host查找MappingHost对象
     * @param scheme 协议名
     * @param host
     * @return
     */
    private MappingHost findMappingBySchemeAndHost(String scheme,String host){
        return findMappingBySchemeAndHost(scheme,host,false);
    }

    /**
     * 根据scheme与host查找MappingHost对象
     * @param scheme 协议名
     * @param host
     * @param force 是否在未查找到的情况下强制返回一个MappingHost对象
     * @return
     */
    private MappingHost findMappingBySchemeAndHost(String scheme,String host,boolean force){
        for(MappingHost mappingHost : mappingHostList){
            if(mappingHost.getHost().equals(host) && mappingHost.getScheme().equals(scheme))
                return mappingHost;
        }
        if(force){
            MappingHost mappingHost = new MappingHost(scheme,host);
            mappingHostList.add(mappingHost);
            return mappingHost;
        }else{
            return null;
        }
    }

    /**
     * 拦截url
     * @param webView
     * @param url
     * @return
     */
    public boolean handle(final WebView webView,String url){
        return handle(webView,url,false);
    }

    /**
     * 拦截url，如果拦截成功，则返回true，未设置拦截，则直接返回false以便于让其它请求能继续走下去
     * @param webView WebView对象实例
     * @param url 请求协议
     * @param jsBridge 是否来自于js桥
     * @return boolean 是否拦截成功
     */
    public boolean handle(final WebView webView, String url, final boolean jsBridge)  {
        //此处必须进行+号处理，否则下文中getQueryParameter时就会将+号Decode为空格
        url = url.replaceAll("\\+","%2B");
        Log.v("info","此请求来自于"+(jsBridge?"jsBridge":"Url"));
        Log.v("info",url);
        Uri uri = Uri.parse(url);
        String host = uri.getHost();
        String path = uri.getPath();
        String scheme = uri.getScheme();
        MappingHost mappingHost = findMappingBySchemeAndHost(scheme,host);
        if(mappingHost == null || path == null || scheme == null) {
            return false;
        }
        MappingPath mappingPath = null;
        List<MappingPath> mappingPaths = mappingHost.getMappingPathList();
        for(MappingPath tmpPath : mappingPaths){
            if(path.equals(tmpPath.getPath())){
                mappingPath = tmpPath;
                break;
            }
        }
        if(mappingPath == null)
            return false;
        //获取回调方法
        final String jsCallback = uri.getQueryParameter("jsCallback");

        //获取参数包
        Set<String> names = uri.getQueryParameterNames();
        Iterator iterator = names.iterator();
        Map<String , Object> params = new HashMap<String,Object>();
        while (iterator.hasNext()){
            String key = (String)iterator.next();
            if(!"jsCallback".equals(key)) {
                params.put(key,uri.getQueryParameter(key));
            }
        }
        try{
            mappingPath.getMethod().invoke(InstancePool.getInstance(mappingPath.getInstanceName()),new Object[]{params,new AppInterfaceCallback() {
                @Override
                public void call(final boolean success, final String message, final JSONObject result) {
                    try{
                        if(jsCallback!=null){
                            if(jsBridge){
                                handler.post(new Runnable(){
                                    @Override
                                    public void run(){
                                        try {
                                            doCall(success, message, result);
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                });
                            }else{
                                doCall(success,message,result);
                            }
                        }

                    }catch (Exception e){
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void call(JSONObject result) {
                    call(true,"",result);
                }

                public void doCall(boolean success,String message,JSONObject result) throws JSONException {
                    webView.loadUrl("javascript:"+jsCallback.replace("{data}",packageData(success,message,result).toString()));
                }
            }});
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        return true;
    }

    /**
     * 打包返回数据
     * @param success 是否成功
     * @param message 消息
     * @param data 数据包
     * @return
     * @throws JSONException
     */
    private JSONObject packageData(boolean success, String message, JSONObject data) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("success",success);
        json.put("code",success?0:1);
        json.put("message",message);
        json.put("data",data);
        return json;
    }

    /*================= 提供一套订阅发布模型 =====================*/

    /**
     * 订阅事件
     * @param eventName 事件名称
     * @param callback 回调
     */
    public AppInterface subscribe(String eventName,Callback callback){
        List<Callback> callbacks = events.get(eventName);
        if(callbacks == null){
            callbacks = new ArrayList<Callback>();
            events.put(eventName, callbacks);
        }
        callbacks.add(callback);
        return this;
    }

    /**
     * 发布事件
     * @param eventName 事件名称
     * @param params 参数包
     */
    public AppInterface notify(String eventName,Map<String,Object> params){
        List<Callback> callbacks = events.get(eventName);
        if(callbacks == null)
            return this;
        for(Callback callback : callbacks){
            callback.call(params);
        }
        return this;
    }

    /**
     * 发布事件
     * @param eventName 事件名称
     */
    public AppInterface notify(String eventName){
        List<Callback> callbacks = events.get(eventName);
        if(callbacks == null)
            return this;
        for(Callback callback : callbacks){
            callback.call(null);
        }
        return this;
    }

    /**
     * 取消订阅事件
     * @param eventName 事件名称
     */
    public AppInterface unsubscribe(String eventName){
        events.remove(eventName);
        return this;
    }

    /**
     * 取消订阅事件
     * @param eventName 事件名称
     * @param callback 回调
     */
    public AppInterface unsubscribe(String eventName,Callback callback){
        List<Callback> callbacks = events.get(eventName);
        if(callbacks != null){
            callbacks.remove(callback);
        }
        return this;
    }

}
