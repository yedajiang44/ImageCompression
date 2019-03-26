package imagecompression.yedajiang44.com.app.integration

import io.reactivex.subjects.Subject

/**
 *  作者：yedajiang44
 *时间 2018-10-23 10:39
 *邮箱：602830483@qq.com
 *说明:实现此接口即可使用RxLifecycle
 */
interface Lifecycleable<T>{
    fun provideLifecycleSubject(): Subject<T>
}