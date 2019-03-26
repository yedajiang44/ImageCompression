package imagecompression.yedajiang44.com

import io.reactivex.Observable
import org.junit.Test
import java.io.File


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        Observable.merge<MutableList<Observable<String>>> { it ->
            val list = mutableListOf<Observable<String>>()
            list.add(Observable.just("1"))
            list.add(Observable.just("2"))
            list.add(Observable.just("3"))
            it.onNext(Observable.just(list))
            it.onComplete()
        }
            .flatMap { it ->
                Observable.combineLatest(it) { it.map { it.toString() } }
            }
            .subscribe {
                println("subscribe:$it")
            }
    }

    /**
     * 作用域
     */
    @Test
    fun scope() {
        fun useRun() {
            val string = "abcd"
            string.run {
                println("this is ${this}") //abcd
                reversed()   //调用一个反向的方法，此时该run方法返回的对象就是reversed处理的值
                //this.reversed() 或者不省略this也行
            }.run {
                println("this is ${this}")  //dcba  将上一个run方法返回的对象打印出来
                length       //调用对象的length此时该run方法返回的对象就是length的值
            }.run {
                println("this is ${this}")  //4   将上一个run方法返回的对象打印出来
            }
        }

        fun useWith() {
            val string = "abcd"
            with(with(with(string) {
                println("this is ${this}")  //abcd
                reversed()
            }) {
                println("this is ${this}")  //dcba
                length
            }) {
                println("this is $this")   //4
            }
        }

        fun useApply() {
            val original = "abc"

            original.apply {
                println("this is ${this}") // "abc"
                reversed()
            }.apply {
                println("this is $this") // "abc"
                length
            }.apply {
                println("this is $this") // abc
            }
        }

        fun useLet() {
            val original = "abc"

            original.let {
                println("it is $it") // "abc"
                it.reversed()
            }.let {
                println("it is $it") // "cba"
                it.length
            }.let {
                println("it is $it") // 3
            }
        }

        fun useAlso() {
            val original = "abc"
            original.also {
                println("it is $it") // "abc"
                it.reversed()
            }.also {
                println("it is $it") // "abc"
                it.length
            }.also {
                println("it is $it") // "abc"
            }
        }

        println("----run------")
        useRun()
        println("----with------")
        useWith()
        println("----apply------")
        useApply()
        println("----let------")
        useLet()
        println("----also------")
        useAlso()
    }

    /**
     * lambda表达式
     */
    @Test
    fun lambda() {
        val sum: (Int, Int) -> Int = { x, y -> x + y }
        println(sum(1, 2))
        fun sum(x: Int, y: Int, onSumListener: (Int, Int) -> Unit): Int {
            return (x + y).apply {
                onSumListener(x + 1, y + 1)
            }
        }
        println(sum(1, 2) { x, y -> println(x + y) })
    }

    /**
     * 可变参数
     */
    @Test
    fun variable() {
        fun sum(vararg nums: Int): Int {//使用关键字vararg
            return nums.sum()
        }
        println(sum(1, 2, 3, 4, 5, 6, 7))
    }

    /**
     * 复制文件
     */
    @Test
    fun copyFileTest() {
        val oldFile = File("E:SQLFULL_CHS.iso")
        val newFile = File("E:SQLFULL_CHS2.iso")
        newFile.deleteOnExit()
        newFile.createNewFile()
        var c = -1
        val buffer = ByteArray(1024 * 1000)
        val inputStream = oldFile.inputStream()
        val now = System.currentTimeMillis()
        while ({ c = inputStream.read(buffer);c }() > 0) {
            newFile.appendBytes(buffer.copyOfRange(0, c))
        }
        println("复制完毕，耗时${(System.currentTimeMillis() - now) / 1000}秒")
    }

    /**
     * 线程
     */
    @Test
    fun thread() {
        Thread {
            println("0,${Thread.currentThread().name},this=$this")
            run {
                println("1,${Thread.currentThread().name},this=$this")
            }
        }.start().run {
            println("2,${Thread.currentThread().name},this=$this")
        }
        println("3,${Thread.currentThread().name},this=$this")
    }

    /**
     *
     */
    @Test
    fun forAndMap() {
        fun cycle(): Boolean {
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .forEach {
                    if (it == 5)
                        return true
                }
            return false
        }

        fun cycle1(): Boolean {
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .map {
                    if (it == 5)
                        return true
                }
            return false
        }
        println(cycle())
        println(cycle1())
    }

}
