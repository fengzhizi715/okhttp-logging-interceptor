package cn.netdiscovery.http.inteerceptor.log

/**
 *
 * @FileName:
 *          cn.netdiscovery.http.inteerceptor.log.LogManager
 * @author: Tony Shen
 * @date: 2020-08-14 17:57
 * @version: V1.0 内部使用的日志操作
 */
object LogManager {

    private var logProxy: LogProxy? = null

    fun logProxy(logProxy: LogProxy) {
        LogManager.logProxy = logProxy
    }

    fun e(tag:String , msg:String) {
        logProxy?.e(tag,msg)
    }

    fun w(tag:String , msg:String) {
        logProxy?.w(tag,msg)
    }

    fun i(tag:String , msg:String) {
        logProxy?.i(tag,msg)
    }

    fun d(tag:String , msg:String) {
        logProxy?.d(tag,msg)
    }
}