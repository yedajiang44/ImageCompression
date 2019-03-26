package imagecompression.yedajiang44.com.app.Utils

import android.app.Activity
import com.google.android.material.snackbar.Snackbar

/**
 *  作者：yedajiang44
 *时间 2018-10-24 19:31
 *邮箱：602830483@qq.com
 *说明:Snackbar工具类
 */
class SnackbarUtils {
    companion object {
        fun showText(activity: Activity, text: String, duration: Int = Snackbar.LENGTH_SHORT) {
            Snackbar.make(
                activity.window.decorView.findViewById(android.R.id.content),
                text,
                duration
            ).show()
        }

        fun showLongText(activity: Activity, text: String, duration: Int = Snackbar.LENGTH_LONG) {
            Snackbar.make(
                activity.window.decorView.findViewById(android.R.id.content),
                text,
                duration
            ).show()
        }
    }
}