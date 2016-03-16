AppInterface — 让JSBridge更简单一点
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
第一个参数代表安卓Context对象，第二个参数是控制器所处包路径，框架会自动扫描此包路径下所有实现了Controller注解的类，并为其建立REST索引与反射并缓存。

###提供两种交互方式
* 使用URL拦截形式，此方式需要在WebViewClient实现类的shouldOverrideUrlLoading方法中进行拦截处理，直接调用
```Java
AppInterface.getInstance().handle(view,url);
```
即可，此方法会返回布尔值，为真代表匹配到了处理器，为假代表未匹配到处理器，理应进行放行。
* 使用JSBridge形式，此方式可以在webView实例化时直接调用
```Java
AppInterface.getInstance().initJsBridge(webView);
```
即可，框架会提供一个名为ApplicationInterface的js对象以供调用，js调用方法为
```javascript 
ApplicationInterface.call(url)。
```
这两种方式可以并存，怎么调都行，拦截器都会拦截并通知相应的Controller进行处理，并统一进行回调处理。

控制器需要继承自BaseController，并在类上加@Controller("host")注解，并且需要在相应方法上加@RequestMapping("/path")注解。
这些方法需要实现两个入参Map&lt;String,Object&gt; params与AppInterfaceCallback callback，前一个是参数包，后一个是回调接口。Controller可以通过调用父类的getContext()方法获取Android上下文对象。此框架建议配合AppInterface.js进行配合使用。

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
即可与APP进行交互，两端都是走的协议形式，不论是通过拦截请求还是JsBridge方式，最终都会解析此协议，并通知相应的Controller进行处理。AppInterface.call()内部会优先使用JsBridge形式，如果APP环境未提供此功能，则使用发送请求的形式，最终达到的效果是一致的，这些对使用者都是不可见的。
