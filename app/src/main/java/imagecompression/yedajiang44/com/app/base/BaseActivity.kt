package imagecompression.yedajiang44.com.app.base

import android.os.Bundle
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.Unbinder
import com.trello.rxlifecycle2.android.ActivityEvent
import imagecompression.yedajiang44.com.app.integration.ActivityLifecycleable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

/**
 *  作者：yedajiang44
 *时间 2018-10-23 10:32
 *邮箱：602830483@qq.com
 *说明:
 */
abstract class BaseActivity : AppCompatActivity(), ActivityLifecycleable {
    private var unbinder: Unbinder? = null
    private val mLifecycleSubject = BehaviorSubject.create<ActivityEvent>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            val layoutResID = initView(savedInstanceState)
            //如果initView返回0,框架则不会调用setContentView(),当然也不会 Bind ButterKnife
            if (layoutResID != 0) {
                setContentView(layoutResID)
                //绑定到butterknife
                unbinder = ButterKnife.bind(this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        initData(savedInstanceState)
    }

    /**
     * 获取布局资源
     */
    abstract fun initView(savedInstanceState: Bundle?): Int

    /**
     * 设置布局后调用
     */
    abstract fun initData(savedInstanceState: Bundle?)

    /**
     * 解绑ButterKnife
     */
    override fun onDestroy() {
        super.onDestroy()
        if (unbinder != null && unbinder !== Unbinder.EMPTY)
            unbinder?.unbind()
        unbinder = null
    }

    /**
     * Subject提供者
     */
    @NonNull
    override fun provideLifecycleSubject(): Subject<ActivityEvent> {
        return mLifecycleSubject
    }
}