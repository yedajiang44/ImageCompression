package imagecompression.yedajiang44.com.mvp.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import imagecompression.yedajiang44.com.R


/**
 *  作者：yedajiang44
 *时间 2018-10-24 14:33
 *邮箱：602830483@qq.com
 *说明:
 */
class EmptyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var paint: Paint = Paint()

    init {
        paint.color = resources.getColor(R.color.colorPrimary)
        paint.textSize = 50f
        paint.style = Paint.Style.FILL
        //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
        paint.textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val fontMetrics = paint.fontMetrics
        val fontTop = fontMetrics.top//为基线到字体上边框的距离,即上图中的top
        val fontBottom = fontMetrics.bottom//为基线到字体下边框的距离,即上图中的bottom
        canvas?.drawText(
            context.resources.getString(R.string.no_preview_yet),
            ((right - left) / 2).toFloat(),
            ((bottom - top) / 2).toFloat() - fontTop / 2 - fontBottom / 2,
            paint
        )
    }
}