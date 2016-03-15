<style>
    .tl{
        text-align: left;
    }
</style>
<div class="container new-discussion-timeline experiment-repo-nav">
    <div class="repository-content">


        <div id="readme" class="readme blob instapaper_body">
            <article class="markdown-body entry-content" itemprop="text">
                <h1><a id="user-content-jquery--new-wave-javascript" class="anchor" href="#jquery--new-wave-javascript"
                       aria-hidden="true">
                    <svg aria-hidden="true" class="octicon octicon-link" height="16" role="img" version="1.1"
                         viewBox="0 0 16 16" width="16">
                        <path d="M4 9h1v1h-1c-1.5 0-3-1.69-3-3.5s1.55-3.5 3-3.5h4c1.45 0 3 1.69 3 3.5 0 1.41-0.91 2.72-2 3.25v-1.16c0.58-0.45 1-1.27 1-2.09 0-1.28-1.02-2.5-2-2.5H4c-0.98 0-2 1.22-2 2.5s1 2.5 2 2.5z m9-3h-1v1h1c1 0 2 1.22 2 2.5s-1.02 2.5-2 2.5H9c-0.98 0-2-1.22-2-2.5 0-0.83 0.42-1.64 1-2.09v-1.16c-1.09 0.53-2 1.84-2 3.25 0 1.81 1.55 3.5 3 3.5h4c1.45 0 3-1.69 3-3.5s-1.5-3.5-3-3.5z"></path>
                    </svg>
                </a><a href="#">AppInterface</a> — 让JSBridge更简单一点
                </h1>

                <h2><a id="user-content-contribution-guides" class="anchor" href="#contribution-guides"
                       aria-hidden="true">
                    <svg aria-hidden="true" class="octicon octicon-link" height="16" role="img" version="1.1"
                         viewBox="0 0 16 16" width="16">
                        <path d="M4 9h1v1h-1c-1.5 0-3-1.69-3-3.5s1.55-3.5 3-3.5h4c1.45 0 3 1.69 3 3.5 0 1.41-0.91 2.72-2 3.25v-1.16c0.58-0.45 1-1.27 1-2.09 0-1.28-1.02-2.5-2-2.5H4c-0.98 0-2 1.22-2 2.5s1 2.5 2 2.5z m9-3h-1v1h1c1 0 2 1.22 2 2.5s-1.02 2.5-2 2.5H9c-0.98 0-2-1.22-2-2.5 0-0.83 0.42-1.64 1-2.09v-1.16c-1.09 0.53-2 1.84-2 3.25 0 1.81 1.55 3.5 3 3.5h4c1.45 0 3-1.69 3-3.5s-1.5-3.5-3-3.5z"></path>
                    </svg>
                </a>简介
                </h2>

                <p>基于安卓实现的一个通过拦截请求与JSBridge的小框架，纯REST风格，基于注解与反射实现，类似于SpringMVC的Controller风格</p>

                <h2><a id="user-content-contribution-guides" class="anchor" href="#contribution-guides"
                       aria-hidden="true">
                    <svg aria-hidden="true" class="octicon octicon-link" height="16" role="img" version="1.1"
                         viewBox="0 0 16 16" width="16">
                        <path d="M4 9h1v1h-1c-1.5 0-3-1.69-3-3.5s1.55-3.5 3-3.5h4c1.45 0 3 1.69 3 3.5 0 1.41-0.91 2.72-2 3.25v-1.16c0.58-0.45 1-1.27 1-2.09 0-1.28-1.02-2.5-2-2.5H4c-0.98 0-2 1.22-2 2.5s1 2.5 2 2.5z m9-3h-1v1h1c1 0 2 1.22 2 2.5s-1.02 2.5-2 2.5H9c-0.98 0-2-1.22-2-2.5 0-0.83 0.42-1.64 1-2.09v-1.16c-1.09 0.53-2 1.84-2 3.25 0 1.81 1.55 3.5 3 3.5h4c1.45 0 3-1.69 3-3.5s-1.5-3.5-3-3.5z"></path>
                    </svg>
                </a>使用指南
                </h2>

                <p>在MainActivity的onCreate方法中通过使用：</p>
                <div class="highlight highlight-source-shell">
                    <pre class="tl">
                        AppInterface.getInstance().init(this,"com.webview.sniyve.webview.controllers");
                    </pre>
                </div>
                <p>进行初始化工作。
                第一个参数代表Context对象，第二个参数是控制器所处包路径，框架会自动扫描此包路径下所有实现了Controller注解的类
                ，并为其建立REST索引与反射并缓存。</p>
                <p>提供两种交互方式</p>
                <ol>
                    <li><p>使用URL拦截形式，此方式需要在WebViewClient实现类的shouldOverrideUrlLoading方法中进行拦截处理，直接调用</p>

                        <div class="highlight highlight-source-shell">
                            <pre class="tl">
                                AppInterface.getInstance().handle(view,url);
                            </pre>
                        </div>
                        <p>即可，此方法会返回布尔值，为真代表匹配到了处理器，为假代表未匹配到处理器，理应进行放行。</p></li>
                    <li><p>使用JSBridge形式，此方式可以在webView实例化时直接调用</p>
                        <div class="highlight highlight-source-shell">
                            <pre class="tl">
                                AppInterface.getInstance().initJsBridge(webView);
                            </pre>
                        </div>
                        <p>即可，框架会提供一个名为ApplicationInterface的js对象以供调用，js调用方法为ApplicationInterface.call(url)。</p></li>
                </ol>
                <p>这两种方式可以并存，怎么调都行，拦截器都会拦截并通知相应的Controller进行处理，并统一进行回调处理。</p>
                <p>Controller类需要继承自BaseController，并在类上加上@Controller("host")注解，并且需要在相应方法上加上@RequestMapping("/path")注解。
                    这些方法需要实现两个入参Map<String,Object> params与AppInterfaceCallback callback，前一个是参数包，后一个
                    是回调接口。Controller可以通过调用父类的getContext()方法获取Android上下文对象。</p>
                <p>此框架建议配合AppInterface.js进行配合使用。</p>
            </article>
        </div>
    </div>
</div>