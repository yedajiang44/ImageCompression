package imagecompression.yedajiang44.com.app.Utils

import android.util.Log

/**
 *  作者：yedajiang44
 *时间 2018-11-14 10:21
 *邮箱：602830483@qq.com
 *说明:
 */

class Log {
    companion object {
        fun d(message: String) = Log.d(Const.LogTag, message)
        fun i(message: String) = Log.i(Const.LogTag, message)
        fun w(message: String) = Log.w(Const.LogTag, message)
        fun e(message: String) = Log.e(Const.LogTag, message)
    }
}