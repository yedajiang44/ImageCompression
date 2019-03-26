package imagecompression.yedajiang44.com.mvp.model.entity

/**
 *  作者：yedajiang44
 *时间 2018-11-17 09:54
 *邮箱：602830483@qq.com
 *说明:压缩模式
 */
enum class CompressedMode {
    /**
     * 尺寸压缩
     */
    SIZE,
    /**
     * 质量压缩
     */
    QUALITY,
    /**
     * 尺寸和质量结合压缩
     */
    SIZE_QUALITY
}