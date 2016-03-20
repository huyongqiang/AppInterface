[AppInterface](https://github.com/yanglang1987500/AppInterface) — 让JSBridge更简单一点
==================================================

简介
----

基于安卓实现的一个通过拦截请求与JSBridge的小框架，纯REST风格，基于注解与反射实现，类似于SpringMVC的Controller风格

使用指南
----

通过使用：
```Java
AppInterface.getInstance().init(this,"com.webview.sniyve.webview.controllers");
```
进行初始化工作。
第一个参数代表安卓`Context`对象，第二个参数是控制器所处包路径，框架会自动扫描此包路径下所有实现了`Controller`注解的类，并为其建立REST索引与反射并缓存。

###提供两种交互方式
* 使用URL拦截形式，此方式需要在`WebViewClient`实现类的`shouldOverrideUrlLoading`方法中进行拦截处理，直接调用
```Java
AppInterface.getInstance().handle(view,url);
```
即可，此方法会返回布尔值，为真代表匹配到了处理器，为假代表未匹配到处理器，理应进行放行。
* 使用JSBridge形式，此方式可以在webView实例化时直接调用
```Java
AppInterface.getInstance().initJsBridge(webView);
```
即可，框架会提供一个名为`ApplicationInterface`的js对象以供调用，js调用方法为
```javascript 
ApplicationInterface.call(url)。
```
这两种方式可以并存，怎么调都行，拦截器都会拦截并通知相应的Controller进行处理，并统一进行回调处理。

###控制器写法
控制器需要继承自`BaseController`，并在类上加`@Controller("host")`注解，并且需要在相应方法上加`@RequestMapping("/path")`注解。
待映射的协议方法需要实现两个入参`Map<String,Object> params`与`AppInterfaceCallback callback`，前一个是参数包，后一个是回调接口。`Controller`可以通过调用父类的`getContext()`方法获取Android上下文对象。<br>
使用示例如下：
```Java
@Controller("common")
public class CommonController extends BaseController{

    @RequestMapping("/toast")
    public void toast(Map<String,Object> params,AppInterfaceCallback callback){
        String message = (String)params.get("msg");
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
        callback.call(null);
    }
}
```
如此便实现了一个简单的`common/toast`协议，所有参数都在params对象中，callback用于执行回调。
此框架建议与AppInterface.js进行搭配使用。

###新增了一套框架的广播订阅机制

test包中已经给出了使用示例<br>
具体使用方法如下：<br>
订阅：
    
```Java
AppInterface.getInstance().subscribe("onClick", new Callback() {
        @Override
        public void call(Map<String, Object> params) {
            TextView textView = (TextView) MainActivity.this.findViewById(R.id.textView);
            textView.setText((String) params.get("value"));
        }
    });
```
发布：
```Java
@RequestMapping("/order_manage")
public void orderManage(Map<String,Object> params, final AppInterfaceCallback callback){
        Map<String,Object> pms = new HashMap<String,Object>();
        pms.put("value", params.get("shopId"));
        AppInterface.getInstance().notify("onClick",pms);
    }
}
```
此模式可以解决不同类或不同Activity中互通事件的问题。

关于性能
-----
实测，Nexus4手机，框架初始化12个Controller，35个协议约35ms-70ms时间，拦截请求耗时（已缓存反射的情况下）为1ms，非常快！
如此便实现了效率高，全自动的目的。


关于AppInterface.js
-----

AppInterface.js是一个通过js调用APP的工具包，内置了一套广播订阅机制与相关接口。但是调用APP不用关心广播，只需要使用
```javascript
AppInterface.call('/common/login',{
  	user:'test',
  	password:'test'
},function(data){
  	console.log(data.message);
});
```
即可与APP进行交互，两端都是走的协议形式，不论是通过拦截请求还是JsBridge方式，最终都会解析此协议，并通知相应的Controller进行处理。`AppInterface.call()`内部会优先使用JsBridge形式，如果APP环境未提供此功能，则使用发送请求的形式，最终达到的效果是一致的，AppInterface.js所提供的call功能不论是对于安卓还是对于iOS都是兼容的。

<br><br><br>最终的目标就是js端只需要按照约定的协议调用call方法，App端只需要实现相应协议的Controller类与协议方法即可。
