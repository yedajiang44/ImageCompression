package imagecompression.yedajiang44.com.app.Utils

import android.content.Context
import imagecompression.yedajiang44.com.R
import io.reactivex.Observable
import org.devio.takephoto.model.TImage
import top.zibin.luban.Luban
import java.io.File
import java.util.*

/**
 *  作者：yedajiang44
 *时间 2018-10-27 14:56
 *邮箱：602830483@qq.com
 *说明:
 */
class CompressorUtils private constructor(val context: Context) {
    private var outPath: String = context.getSharedPreferences(Const.SettingsSharedPreferencesName, Context.MODE_PRIVATE).getString(
        context.resources.getString(
            R.string.choose_out_file_path_key
        ), context.obbDir.absolutePath
    )!!
    private var onCompressorAfterListener: OnCompressorAfterListener? = null
    private var onCompressorBeforeListener: OnCompressorBeforeListener? = null

    class Builder constructor(private val context: Context) {
        private var onCompressorAfterListener: OnCompressorAfterListener? = null
        private var onCompressorBeforeListener: OnCompressorBeforeListener? = null

        fun OnCompressorAfter(listener: OnCompressorAfterListener): Builder {
            onCompressorAfterListener = listener
            return this
        }

        fun OnCompressorBefore(listener: OnCompressorBeforeListener): Builder {
            onCompressorBeforeListener = listener
            return this
        }

        fun buileder(): Observable<CompressorUtils> {
            val utils = CompressorUtils(context)
            utils.onCompressorAfterListener = onCompressorAfterListener
            utils.onCompressorBeforeListener = onCompressorBeforeListener
            return Observable.just(utils)
        }
    }

    /**
     * 使用鲁班压缩
     * @param images 待压缩的图片集合
     */
    fun byLuban(
        images: List<TImage>
    ): Observable<List<TImage>> {
        return Observable.merge<ArrayList<Observable<TImage>>> {
            val sources = ArrayList<Observable<TImage>>()
            images.forEachIndexed { index, image ->
                if (onCompressorBeforeListener != null)
                    onCompressorBeforeListener?.onCompressorBefore(index, images)
                image.compressPath =
                        Luban.with(context).load(image.originalPath).setTargetDir(outPath).get().first().path
                image.isCompressed = true
                if (onCompressorAfterListener != null)
                    onCompressorAfterListener?.onCompressorAfter(index, images)
                sources.add(Observable.just(image))
            }
            it.onNext(Observable.just(sources))
            it.onComplete()
        }.flatMap { it -> Observable.combineLatest(it) { list -> list.map { it as TImage } } }
    }

    /**
     * 使用libjpeg-turbo压缩
     * @param images 待压缩的图片集合
     * @param quality 压缩率，默认为80，数值越大压缩后图片质量越差
     */
    fun byNativeTurbo(
        images: List<TImage>,
        quality: Int = 80
    ): Observable<List<TImage>> {
        val turbo = Turbo()
        return Observable.merge<ArrayList<Observable<TImage>>> {
            val sources = ArrayList<Observable<TImage>>()
            images.forEachIndexed { index, image ->
                if (onCompressorBeforeListener != null)
                    onCompressorBeforeListener?.onCompressorBefore(index, images)
                val fileName = UUID.randomUUID().toString() + "." + image.originalPath.split(".").last()
                if ({
                        image.isCompressed = turbo.compress(
                            File(image.originalPath).inputStream(),
                            100 - quality,
                            "$outPath/$fileName"
                        );image.isCompressed
                    }()) {
                    image.compressPath = "$outPath/$fileName"
                    copyExif(image.originalPath, image.compressPath)
                }
                if (onCompressorAfterListener != null)
                    onCompressorAfterListener?.onCompressorAfter(index, images)
                sources.add(Observable.just(image))
            }
            it.onNext(Observable.just(sources))
            it.onComplete()
        }.flatMap { it -> Observable.combineLatest(it) { list -> list.map { it as TImage } } }
    }

    /**
     * 复制原图Exif信息到压缩后图片的Exif信息
     * 注意：使用了反射
     * @param sourcePath 原图路径
     * @param targetPath 目标图片路径
     */
    private fun copyExif(sourcePath: String, targetPath: String) {
        try {
            val source = androidx.exifinterface.media.ExifInterface(sourcePath)
            val target = androidx.exifinterface.media.ExifInterface(targetPath)
            source.javaClass.declaredFields.forEach { member ->
                member.isAccessible = true
                val tag = member.get(source)
                if (member.name.startsWith("TAG_") && tag is String) {
                    target.setAttribute(tag, source.getAttribute(tag))
                    target.saveAttributes()
                }
            }
        } catch (e: Exception) {
        }
    }

    interface OnCompressorAfterListener {
        fun onCompressorAfter(position: Int, images: List<TImage>)
    }

    interface OnCompressorBeforeListener {
        fun onCompressorBefore(position: Int, images: List<TImage>)
    }
}
