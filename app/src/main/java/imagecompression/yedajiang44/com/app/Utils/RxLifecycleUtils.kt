package imagecompression.yedajiang44.com.app.Utils

import android.view.View
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import com.trello.rxlifecycle2.internal.Preconditions
import imagecompression.yedajiang44.com.app.integration.ActivityLifecycleable
import imagecompression.yedajiang44.com.app.integration.FragmentLifecycleable
import imagecompression.yedajiang44.com.app.integration.Lifecycleable
import io.reactivex.annotations.NonNull

/**
 *  作者：yedajiang44
 *时间 2018-10-23 10:25
 *邮箱：602830483@qq.com
 *说明:RxLifecycle工具类
 */

class RxLifecycleUtils private constructor() {

    init {
        throw IllegalStateException("you can't instantiate me!")
    }

    companion object {

        /**
         * 绑定 Activity 的指定生命周期
         *
         * @param view
         * @param event
         * @param <T>
         * @return
        </T> */
        fun <T> bindUntilEvent(
            @NonNull view: View,
            event: ActivityEvent
        ): LifecycleTransformer<T> {
            Preconditions.checkNotNull(view, "view == null")
            return if (view is ActivityLifecycleable) {
                bindUntilEvent(view as ActivityLifecycleable, event)
            } else {
                throw IllegalArgumentException("view isn't ActivityLifecycleable")
            }
        }

        /**
         * 绑定 Fragment 的指定生命周期
         *
         * @param view
         * @param event
         * @param <T>
         * @return
        </T> */
        fun <T> bindUntilEvent(
            @NonNull view: View,
            event: FragmentEvent
        ): LifecycleTransformer<T> {
            Preconditions.checkNotNull(view, "view == null")
            return if (view is FragmentLifecycleable) {
                bindUntilEvent(view as FragmentLifecycleable, event)
            } else {
                throw IllegalArgumentException("view isn't FragmentLifecycleable")
            }
        }

        fun <T, R> bindUntilEvent(
            @NonNull lifecycleable: Lifecycleable<R>,
            event: R
        ): LifecycleTransformer<T> {
            Preconditions.checkNotNull(lifecycleable, "lifecycleable == null")
            return RxLifecycle.bindUntilEvent(lifecycleable.provideLifecycleSubject(), event)
        }


        /**
         * 绑定 Activity/Fragment 的生命周期
         *
         * @param view
         * @param <T>
         * @return
        </T> */
        fun <T> bindToLifecycle(@NonNull view: View): LifecycleTransformer<T> {
            Preconditions.checkNotNull(view, "view == null")
            return if (view is Lifecycleable<*>) {
                bindToLifecycle(view as Lifecycleable<*>)
            } else {
                throw IllegalArgumentException("view isn't Lifecycleable")
            }
        }

        fun <T> bindToLifecycle(@NonNull lifecycleable: Lifecycleable<*>): LifecycleTransformer<T> {
            Preconditions.checkNotNull(lifecycleable, "lifecycleable == null")
            return when (lifecycleable) {
                is ActivityLifecycleable -> RxLifecycleAndroid.bindActivity(lifecycleable.provideLifecycleSubject())
                is FragmentLifecycleable -> RxLifecycleAndroid.bindFragment(lifecycleable.provideLifecycleSubject())
                else -> throw IllegalArgumentException("Lifecycleable not match")
            }
        }
    }

}