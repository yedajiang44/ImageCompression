package imagecompression.yedajiang44.com.app.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.trello.rxlifecycle2.android.FragmentEvent
import imagecompression.yedajiang44.com.app.integration.FragmentLifecycleable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

/**
 *  作者：yedajiang44
 *时间 2018-10-23 10:46
 *邮箱：602830483@qq.com
 *说明:
 */
abstract class BaseFragment : Fragment(), FragmentLifecycleable {
    private var unbinder: Unbinder? = null
    private val mLifecycleSubject = BehaviorSubject.create<FragmentEvent>()
    override fun provideLifecycleSubject(): Subject<FragmentEvent> {
        return mLifecycleSubject
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = initView(inflater, container, savedInstanceState)
        if (view != null)
            unbinder = ButterKnife.bind(view)
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        if (unbinder != null && unbinder !== Unbinder.EMPTY)
            unbinder?.unbind()
        unbinder = null
    }

    abstract fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
}