package cn.netdiscovery.http.interceptor.log.test

import cn.netdiscovery.http.interceptor.LoggingInterceptor
import cn.netdiscovery.http.interceptor.log.LogManager
import cn.netdiscovery.http.interceptor.log.LogProxy
import okhttp3.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 *
 * @FileName:
 *          cn.netdiscovery.http.interceptor.log.test.Test
 * @author: Tony Shen
 * @date: 2020-08-17 14:04
 * @version: V1.0 <描述当前版本功能>
 */
val topLevelClass = object : Any() {}.javaClass.enclosingClass
val logger: Logger = LoggerFactory.getLogger(topLevelClass)

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
        .responseTag("Response") //.hideVerticalLine()// 隐藏竖线边框
        .build()

    //设置拦截器
    builder.addInterceptor(loggingInterceptor)

    builder.build()
}

fun main() {

    LogManager.logProxy(object :LogProxy{
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
        .get() //默认就是GET请求，可以不写
        .build()

    val call: Call = okhttp.newCall(request)
    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
        }

        override fun onResponse(call: Call, response: Response) {
            logger.info("code:"+response.code)
        }
    })
}