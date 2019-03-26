package imagecompression.yedajiang44.com.mvp.ui.widget

import android.content.Context
import androidx.annotation.Nullable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView


/**
 *  作者：yedajiang44
 *时间 2018-10-24 19:02
 *邮箱：602830483@qq.com
 *说明:上滑不冲突的RecyclerView
 */
class UpMoveNoConflictRecyclerView : RecyclerView {
    private var mStartX: Float = 0.toFloat()
    private var mStartY: Float = 0.toFloat()

    constructor(context: Context) : super(context)

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mStartX = ev.rawX
                mStartY = ev.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                val endY = ev.rawY
                val endX = ev.rawX
                val x = endX - mStartX
                val y = endY - mStartY
                /* 左、右、下滑动不拦截，上滑拦截*/
                if (Math.abs(y) > Math.abs(x) && y < 0) {
                    parent.requestDisallowInterceptTouchEvent(true)
                } else {
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

}