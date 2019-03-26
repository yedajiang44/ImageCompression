package imagecompression.yedajiang44.com.mvp.ui.widget

import android.content.Context
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 *  作者：yedajiang44
 *时间 2018-10-25 15:44
 *邮箱：602830483@qq.com
 *说明:
 */
class ImagePreviewPager : ViewPager {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return try {
            super.onInterceptTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}