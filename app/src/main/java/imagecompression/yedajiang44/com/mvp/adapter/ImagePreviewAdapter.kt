package imagecompression.yedajiang44.com.mvp.adapter

import androidx.viewpager.widget.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import org.devio.takephoto.model.TImage

/**
 *  作者：yedajiang44
 *时间 2018-10-25 15:46
 *邮箱：602830483@qq.com
 *说明:
 */
class ImagePreviewAdapter<T> : PagerAdapter() where T : TImage {
    var data = mutableListOf<T>()

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view == any
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = SubsamplingScaleImageView(container.context)
        imageView.orientation = SubsamplingScaleImageView.ORIENTATION_USE_EXIF
        imageView.setImage(ImageSource.uri(data[position].compressPath))
        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}