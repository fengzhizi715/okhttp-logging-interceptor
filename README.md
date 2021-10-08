# okhttp-logging-interceptor

[![@Tony沈哲 on weibo](https://img.shields.io/badge/weibo-%40Tony%E6%B2%88%E5%93%B2-blue.svg)](http://www.weibo.com/fengzhizi715)
[![License](https://img.shields.io/badge/license-Apache%202-lightgrey.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![](https://jitpack.io/v/fengzhizi715/okhttp-logging-interceptor.svg)](https://jitpack.io/#fengzhizi715/okhttp-logging-interceptor)

# 功能特点：

* 支持 Android、桌面、后端项目的使用
* 支持 http request、response 的数据格式化的输出。
* 当请求为 POST 时，支持 Form 表单的打印。
* 支持格式化时去掉竖线边框显示日志，方便将网络请求复制到 Postman 之类的工具。
* 支持日志级别
* 支持显示当前的线程名称
* 支持排除一些接口的日志显示，便于快速的排查问题。
* 支持设置显示 url 的最大长度，超过这个长度则换行。 
* 支持设置每一行的最大长度

# 下载安装：

将它添加到项目的 root build.gradle 中：

```groovy
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```

使用 okhttp-logging-interceptor 的依赖

```groovy
implementation 'com.github.fengzhizi715.okhttp-logging-interceptor:core:v1.1.0'
```

# 使用方法：

将 loggingInterceptor 添加到 builder

```kotlin
val okhttp:OkHttpClient by lazy {

    val builder = OkHttpClient.Builder()
    builder.writeTimeout(30 * 1000.toLong(), TimeUnit.MILLISECONDS)
    builder.readTimeout(20 * 1000.toLong(), TimeUnit.MILLISECONDS)
    builder.connectTimeout(15 * 1000.toLong(), TimeUnit.MILLISECONDS)

    val loggingInterceptor = LoggingInterceptor.Builder()
        .loggable(true) // TODO: 发布到生产环境需要改成false
        .request()
        .requestTag("Request")
        .response()
        .responseTag("Response")
//        .hideVerticalLine()// 隐藏竖线边框
        .build()

    //设置拦截器
    builder.addInterceptor(loggingInterceptor)

    builder.build()
}
```

> 值得注意的是开发者必须要实现 LogProxy 否则无法打印 request、response。
另外，日志代理类可以使用开发者自己的日志库，这样就可以在不同平台使用该库。

```kotlin
    LogManager.logProxy(object :LogProxy{  // 必须要实现 LogProxy ，否则无法打印网络请求的 request 、response
        override fun e(tag: String, msg: String) {
        }

        override fun w(tag: String, msg: String) {
        }

        override fun i(tag: String, msg: String) {
            logger.info("$tag:$msg")
        }

        override fun d(tag: String, msg: String) {
            logger.debug("$tag:$msg")
        }
    })

    val url = "http://wwww.baidu.com"
    val request: Request = Request.Builder()
        .url(url)
        .get()
        .build()

    val call: Call = okhttp.newCall(request)
    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
        }

        override fun onResponse(call: Call, response: Response) {
            logger.info("code:"+response.code)
        }
    })
```

# 效果显示

```
00:28:33.316 [OkHttp http://wwww.baidu.com/...] INFO cn.netdiscovery.http.interceptor.log.test.TestKt - Request:  
╔══════════════════════════════════════════════════════════════════════════════════════════════════
║ URL: http://wwww.baidu.com/
║ 
║ Method: @GET
║ 
║ Thread: OkHttp http://wwww.baidu.com/...
║ 
╚══════════════════════════════════════════════════════════════════════════════════════════════════
00:28:33.505 [OkHttp http://wwww.baidu.com/...] INFO cn.netdiscovery.http.interceptor.log.test.TestKt - Response:  
╔══════════════════════════════════════════════════════════════════════════════════════════════════
║ URL: http://wwww.baidu.com/
║ 
║ is success : true - Received in: 172ms
║ 
║ Status Code: 200
║ 
║ Thread: OkHttp http://wwww.baidu.com/...
║ 
║ Headers:
║ - Cache-Control: private, no-cache, no-store, proxy-revalidate, no-transform
║ - Connection: keep-alive
║ - Content-Type: text/html
║ - Date: Fri, 18 Sep 2020 16:28:33 GMT
║ - Last-Modified: Mon, 23 Jan 2017 13:27:36 GMT
║ - Pragma: no-cache
║ - Server: bfe/1.0.8.18
║ - Set-Cookie: BDORZ=27315; max-age=86400; domain=.baidu.com; path=/
║ - Transfer-Encoding: chunked
║ 
║ Body:
║ <!DOCTYPE html>
║ <!--STATUS OK--><html> <head><meta http-equiv=content-type content=text/html;charset=utf-8><meta http-equiv=X-UA-Compati
║ ble content=IE=Edge><meta content=always name=referrer><link rel=stylesheet type=text/css href=http://s1.bdstatic.com/r/
║ www/cache/bdorz/baidu.min.css><title>百度一下，你就知道</title></head> <body link=#0000cc> <div id=wrapper> <div id=head> <div cl
║ ass=head_wrapper> <div class=s_form> <div class=s_form_wrapper> <div id=lg> <img hidefocus=true src=//www.baidu.com/img/
║ bd_logo1.png width=270 height=129> </div> <form id=form name=f action=//www.baidu.com/s class=fm> <input type=hidden nam
║ e=bdorz_come value=1> <input type=hidden name=ie value=utf-8> <input type=hidden name=f value=8> <input type=hidden name
║ =rsv_bp value=1> <input type=hidden name=rsv_idx value=1> <input type=hidden name=tn value=baidu><span class="bg s_ipt_w
║ r"><input id=kw name=wd class=s_ipt value maxlength=255 autocomplete=off autofocus></span><span class="bg s_btn_wr"><inp
║ ut type=submit id=su value=百度一下 class="bg s_btn"></span> </form> </div> </div> <div id=u1> <a href=http://news.baidu.com
║  name=tj_trnews class=mnav>新闻</a> <a href=http://www.hao123.com name=tj_trhao123 class=mnav>hao123</a> <a href=http://ma
║ p.baidu.com name=tj_trmap class=mnav>地图</a> <a href=http://v.baidu.com name=tj_trvideo class=mnav>视频</a> <a href=http://
║ tieba.baidu.com name=tj_trtieba class=mnav>贴吧</a> <noscript> <a href=http://www.baidu.com/bdorz/login.gif?login&amp;tpl=
║ mn&amp;u=http%3A%2F%2Fwww.baidu.com%2f%3fbdorz_come%3d1 name=tj_login class=lb>登录</a> </noscript> <script>document.write
║ ('<a href="http://www.baidu.com/bdorz/login.gif?login&tpl=mn&u='+ encodeURIComponent(window.location.href+ (window.locat
║ ion.search === "" ? "?" : "&")+ "bdorz_come=1")+ '" name="tj_login" class="lb">登录</a>');</script> <a href=//www.baidu.co
║ m/more/ name=tj_briicon class=bri style="display: block;">更多产品</a> </div> </div> </div> <div id=ftCon> <div id=ftConw> <
║ p id=lh> <a href=http://home.baidu.com>关于百度</a> <a href=http://ir.baidu.com>About Baidu</a> </p> <p id=cp>&copy;2017&nbs
║ p;Baidu&nbsp;<a href=http://www.baidu.com/duty/>使用百度前必读</a>&nbsp; <a href=http://jianyi.baidu.com/ class=cp-feedback>意见反
║ 馈</a>&nbsp;京ICP证030173号&nbsp; <img src=//www.baidu.com/img/gs.gif> </p> </div> </div> </div> </body> </html>
╚══════════════════════════════════════════════════════════════════════════════════════════════════
00:28:33.505 [OkHttp http://wwww.baidu.com/...] INFO cn.netdiscovery.http.interceptor.log.test.TestKt - code:200
```

# 针对 Android 的封装

Android 项目可以直接使用

https://github.com/fengzhizi715/saf-logginginterceptor

# TODO List

* 优化 Android 过长时，最后的部分显示不全的问题。

联系方式
===

Wechat：fengzhizi715

> Java与Android技术栈：每周更新推送原创技术文章，欢迎扫描下方的公众号二维码并关注，期待与您的共同成长和进步。

![](https://github.com/fengzhizi715/NetDiscovery/blob/master/images/gzh.jpeg)

License
-------

    Copyright (C) 2020 - present, Tony Shen.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.