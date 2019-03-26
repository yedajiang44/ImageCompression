package imagecompression.yedajiang44.com.app.Utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.InputStream

/**
 *  作者：yedajiang44
 *时间 2018-11-01 14:58
 *邮箱：602830483@qq.com
 *说明:libjpeg-turbo压缩
 */
class Turbo {
    /**
     * 压缩
     * @param file 文件流
     * @param quality 压缩率，数值越小压缩后的图片质量越低
     * @param outfile 压缩后的图片保存路径
     */
    fun compress(file: InputStream, quality: Int, outfile: String): Boolean {

        val tagBitmap = BitmapFactory.decodeStream(file)

        return nativeCompress(tagBitmap, quality, outfile)
    }

    /**
     * 压缩
     * @param path 文件路径
     * @param quality 压缩率，数值越小压缩后的图片质量越低
     * @param outfile 压缩后的图片保存路径
     */
    fun compress(path: String, quality: Int, outfile: String): Boolean {

        val tagBitmap = BitmapFactory.decodeFile(path)

        return nativeCompress(tagBitmap, quality, outfile)
    }

    private external fun nativeCompress(bitmap: Bitmap?, quality: Int, outfile: String): Boolean

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }
}