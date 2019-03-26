package imagecompression.yedajiang44.com.app.Utils

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 *  作者：yedajiang44
 *时间 2018-10-25 19:51
 *邮箱：602830483@qq.com
 *说明:
 */
abstract class Observer<T> : Observer<T> {
    override fun onComplete() {
    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
    }

}