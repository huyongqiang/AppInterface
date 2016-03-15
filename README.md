# AndroidInterface
基于安卓实现的一个通过拦截请求与JSBridge的小框架，纯REST风格，基于注解与反射实现，类似于SpringMVC的Controller风格

使用方式：
    在MainActivity的onCreate方法中
        AppInterface.getInstance().init(this,"com.webview.sniyve.webview.controllers");
    第一个参数代表Context对象，第二个参数是控制器所处理包路径，框架会自动扫描此包路径下的实现了Controller注解的类
    ，并为其建立REST索引与反射缓存。
    
    提供两种交互方式
    一，
      使用URL拦截形式，此方式需要在WebViewClient实现类的shouldOverrideUrlLoading方法中进行拦截处理，直接调用
      AppInterface.getInstance().handler(view,url)即可，此方法会返回布尔值，为真代表匹配到了处理器，为假代表
      未匹配到处理器，理应进行放行。
    二，
      使用JSBridge形式，此方式可以webView实例化时直接调用AppInterface.getInstance().init(webView);即可，框架
      会提供一个AndroidInterface js对象以供调用，方法为AndroidInterface.call(url)即可。
    
    这两种方式可以并存，你想怎么调都行，拦截器都会拦截并通知相应的Controller进行处理，并进行统一进行回调。
    

    Controller类需要继承自BaseController，并在类上加上@Controller("host")注解，并且需要在相应方法上加上@RequestMapping("/path")注解
    这些方法需要实现两个入参Map<String,Object> params与AppInterfaceCallback callback，前一个是参数包，后一个
    是回调接口。Controller可以通过调用父类的getContext()方法获取Android上下文对象。
      
    此框架建议配合我的另一外js项目AppInterface.js进行配合使用。
