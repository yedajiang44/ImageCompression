package imagecompression.yedajiang44.com.mvp.ui.activity

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import imagecompression.yedajiang44.com.R
import imagecompression.yedajiang44.com.app.Utils.Const
import imagecompression.yedajiang44.com.mvp.adapter.ImagePreviewAdapter
import kotlinx.android.synthetic.main.activity_image_loader.*
import org.devio.takephoto.model.TImage
import java.io.File


class ImagePreviewActivity : Activity() {
    private var mAdapter = ImagePreviewAdapter<TImage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_loader)
        initViewPager()
        setHideVirtualKey()
        window.decorView.setOnSystemUiVisibilityChangeListener { setHideVirtualKey() }//设置当虚拟键隐藏状态被改变时继续隐藏
    }

    /**
     * 初始化viewpager
     */
    private fun initViewPager() {
        val json = intent.getStringExtra(Const.ImageKey)
        mAdapter.data = Gson().fromJson(json, object : TypeToken<List<TImage>>() {}.type)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(position: Int) {
                setTitleText(position)
            }
        })
        viewPager.adapter = mAdapter
        setTitleText(0)
    }

    /**
     * 设置标题文本
     * @param position viewpager的position
     */
    private fun setTitleText(position: Int) {
        val image = mAdapter.data[position]
        val original = File(image.originalPath)
        val compress = File(image.compressPath)
        titleView.text = String.format(
            resources.getString(R.string.image_size_detail),
            compress.length() / 1024f,
            original.length() / 1024f
        )
    }

    /**
     * 隐藏虚拟键
     */
    private fun setHideVirtualKey() {
        if (Build.VERSION.SDK_INT in 12..18) {
            val v = window.decorView
            v.systemUiVisibility = View.GONE
        } else if (Build.VERSION.SDK_INT >= 19) {
            val decorView = window.decorView
            val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            decorView.systemUiVisibility = uiOptions
        }
    }
}
