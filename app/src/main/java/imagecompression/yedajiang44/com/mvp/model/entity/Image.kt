package imagecompression.yedajiang44.com.mvp.model.entity

/**
 *  作者：yedajiang44
 *时间 2018-10-26 10:22
 *邮箱：602830483@qq.com
 *说明: 图片实体类
 */
/**
 * @param originalPath 原始路径
 * @param compressPath 压缩路径
 * @param fromType
 */
data class Image(var originalPath: String, var compressPath: String,var fromType:FromType,var cropped:Boolean,var compressed:Boolean) {
    enum class FromType {
        CAMERA, OTHER
    }
}
