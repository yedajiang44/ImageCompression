package imagecompression.yedajiang44.com

import androidx.exifinterface.media.ExifInterface
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import android.util.Log
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("imagecompression.yedajiang44.com", appContext.packageName)
    }

    @Test
    fun exifInterface() {
        val exifInterface= androidx.exifinterface.media.ExifInterface("/storage/9016-4EF8/DCIM/100ANDRO/DSC_0926.JPG")
//         exifInterface.javaClass.declaredFields.forEach {field->
//             try {
//                 field.isAccessible = true // 如果不设置不能获对应的值
//                 val fn = field.name
//                 Log.e(javaClass.simpleName, "属性名称：$fn  field.get(obj)= ${field.get(exifInterface)}")
//             } catch (e: IllegalAccessException) {
//                 e.printStackTrace()
//             }
//        }
        exifInterface.javaClass.declaredFields.forEach { member ->
            member.isAccessible = true
            if (member.name.startsWith("TAG_")&&member.get(exifInterface) is String) {
                Log.e(javaClass.simpleName,member.get(exifInterface).toString())
            }
        }
    }
    class TestUtil {
        fun reflex(obj: Any) {
            val aClass = obj.javaClass
//    aClass.fields 无法获取 kotlin 创建的成员
            val declaredFields = aClass.declaredFields
            val methods = aClass.methods
            for (mt in methods) {
                val mn = mt.name
                val dv = mt.defaultValue
                val returnType = mt.returnType.name
                System.err.println("方法名称：$mn    默认值：$dv    返回类型：$returnType")
            }
            for (field in declaredFields) {
                try {
                    field.isAccessible = true // 如果不设置不能获对应的值
                    val fn = field.name
                    Log.e(javaClass.simpleName, "属性名称：$fn  field.get(obj)= ${field.get(obj)}")
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
