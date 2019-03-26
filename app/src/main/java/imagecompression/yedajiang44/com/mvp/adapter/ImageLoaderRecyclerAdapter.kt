package imagecompression.yedajiang44.com.mvp.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.chad.library.adapter.base.BaseItemDraggableAdapter
import com.chad.library.adapter.base.BaseViewHolder
import imagecompression.yedajiang44.com.R
import imagecompression.yedajiang44.com.mvp.model.entity.SupportFormat
import org.devio.takephoto.model.TImage


/**
 *  作者：yedajiang44
 *时间 2018-10-23 16:38
 *邮箱：602830483@qq.com
 *说明:
 */
class ImageLoaderRecyclerAdapter :
    BaseItemDraggableAdapter<TImage, BaseViewHolder>(R.layout.recycler_item_image, null) {
    private lateinit var requestManager: RequestManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        requestManager = Glide.with(parent.context)
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun convert(helper: BaseViewHolder?, item: TImage?) {
        requestManager.load(item?.originalPath).into(helper?.getView(R.id.image) as ImageView)
        val textView = helper.getView<TextView>(R.id.text)
        if (SupportFormat.isSupport(item?.originalPath.toString()))
            textView.visibility = View.GONE
        else
            textView.visibility = View.VISIBLE
//        val image = helper?.getView<SubsamplingScaleImageView>(R.id.image)
//        image?.orientation = SubsamplingScaleImageView.ORIENTATION_0
//        image?.setImage(ImageSource.uri(item?.originalPath.toString()))
    }
}
