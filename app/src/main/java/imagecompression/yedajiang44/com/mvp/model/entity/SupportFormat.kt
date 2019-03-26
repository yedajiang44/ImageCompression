package imagecompression.yedajiang44.com.mvp.model.entity

import io.reactivex.annotations.NonNull

/**
 *  作者：yedajiang44
 *时间 2018-10-24 10:48
 *邮箱：602830483@qq.com
 *说明:支持的图片格式
 */
enum class SupportFormat {
    /**
     * png格式
     */
    PNG,
    /**
     * jpg格式
     */
    JPG,
    /**
     * jpeg格式
     */
    JPEG;

    companion object {
        /**
         * 判断是否为支持的格式
         * @param filePath 文件绝对路径
         * @return 是否支持
         */
        fun isSupport(@NonNull filePath: String): Boolean {
            enumValues<SupportFormat>().map { format ->
                if (filePath.toLowerCase().endsWith(format.name.toLowerCase()))
                    return true
            }
            return false
        }
    }
}