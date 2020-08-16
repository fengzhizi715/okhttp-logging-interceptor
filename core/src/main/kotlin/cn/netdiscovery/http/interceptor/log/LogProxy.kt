package cn.netdiscovery.http.interceptor.log

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.interceptor.log.LogProxy
 * @author: Tony Shen
 * @date: 2020-08-14 17:56
 * @version: V1.0 日志库的代理操作，便于开发者使用自己的日志库
 */
interface LogProxy {

    fun e(tag:String , msg:String)

    fun w(tag:String , msg:String)

    fun i(tag:String , msg:String)

    fun d(tag:String , msg:String)
}