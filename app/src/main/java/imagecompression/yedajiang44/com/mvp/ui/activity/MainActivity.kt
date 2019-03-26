package imagecompression.yedajiang44.com.mvp.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.OnClick
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback
import com.google.gson.Gson
import com.tbruyelle.rxpermissions2.RxPermissions
import imagecompression.yedajiang44.com.R
import imagecompression.yedajiang44.com.app.Utils.*
import imagecompression.yedajiang44.com.app.base.BaseActivity
import imagecompression.yedajiang44.com.mvp.adapter.ImageLoaderRecyclerAdapter
import imagecompression.yedajiang44.com.mvp.model.entity.CompressedMode
import imagecompression.yedajiang44.com.mvp.model.entity.SupportFormat
import imagecompression.yedajiang44.com.mvp.ui.widget.EmptyView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.devio.takephoto.app.TakePhoto
import org.devio.takephoto.app.TakePhotoImpl
import org.devio.takephoto.model.InvokeParam
import org.devio.takephoto.model.TImage
import org.devio.takephoto.model.TResult
import org.devio.takephoto.permission.InvokeListener
import org.devio.takephoto.permission.PermissionManager
import org.devio.takephoto.permission.TakePhotoInvocationHandler
import org.lzh.framework.updatepluginlib.UpdateBuilder
import org.lzh.framework.updatepluginlib.UpdateConfig
import org.lzh.framework.updatepluginlib.base.UpdateParser
import org.lzh.framework.updatepluginlib.model.Update
import java.io.File


/**
 *  作者：yedajiang44
 *时间 2018-10-22 10:17
 *邮箱：602830483@qq.com
 *说明:
 */
class MainActivity : BaseActivity(), TakePhoto.TakeResultListener, InvokeListener {
    private var mExitTime: Long = 0

    private var takePhoto: TakePhoto = TakePhotoInvocationHandler.of(this).bind(TakePhotoImpl(this, this)) as TakePhoto

    private val mAdapter = ImageLoaderRecyclerAdapter()

    private var loadingDialog: AlertDialog? = null

    override fun initView(savedInstanceState: Bundle?): Int {
        takePhoto.onCreate(savedInstanceState)
        return R.layout.activity_main
    }

    override fun initData(savedInstanceState: Bundle?) {
        initUpdatePlugin()
        initToolbar()
        initImageListView()
    }

    private fun initUpdatePlugin() {
        UpdateConfig.getConfig()
            .setUrl(Const.UpdateUrl).updateParser = object : UpdateParser() {
            override fun parse(response: String?): Update {
                return Gson().fromJson(response, Update::class.java)
            }
        }
        UpdateBuilder.create().check()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
    }

    /**
     * 初始化图片列表
     */
    private fun initImageListView() {
        imageList.layoutManager = LinearLayoutManager(this)
        (imageList.layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL

        mAdapter.emptyView = EmptyView(this)
        mAdapter.emptyView.setBackgroundColor(resources.getColor(R.color.colorTransparentGray))
        val callback = ItemDragAndSwipeCallback(mAdapter)
        callback.setSwipeMoveFlags(ItemTouchHelper.UP)//设置上划删除
        ItemTouchHelper(callback).attachToRecyclerView(imageList)
        mAdapter.enableSwipeItem()
        imageList.adapter = mAdapter
    }

    /**
     * 相册按钮点击事件
     */
    @SuppressLint("CheckResult")
    @OnClick(R.id.album)
    fun albumClick() {
        RxPermissions(this)
            .request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .subscribe { permission ->
                if (permission) {
                    takePhoto.onPickMultiple(Int.MAX_VALUE)
                } else {
                    SnackbarUtils.showText(this, resources.getString(R.string.noRelevantPermissions))
                }
            }
    }

    /**
     * 相机按钮点击事件
     */
    @SuppressLint("CheckResult")
    @OnClick(R.id.screen)
    fun screenClick() {
        RxPermissions(this)
            .request(
                Manifest.permission.CAMERA
            )
            .subscribe { permission ->
                if (permission)
                    takePhoto.onPickFromCapture(
                        Uri.fromFile(
                            File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath,
                                "${System.currentTimeMillis()}.jpg"
                            )
                        )
                    )
                else
                    SnackbarUtils.showText(this, resources.getString(R.string.noRelevantPermissions))
            }
    }

    /**
     * 压缩按钮点击事件
     */
    @OnClick(R.id.compression)
    fun compressionClick() {
        val compressionRatio = getSharedPreferences(Const.SettingsSharedPreferencesName, Context.MODE_PRIVATE).getInt(
            resources.getString(R.string.compression_ratio_key),
            resources.getString(R.string.compression_ratio_default_value).toInt()
        )
        val compressedMode = getSharedPreferences(Const.SettingsSharedPreferencesName, Context.MODE_PRIVATE).getString(
            resources.getString(R.string.compressed_mode_key),
            resources.getString(R.string.compressed_mode_default_value)
        ) ?: CompressedMode.QUALITY.name
        Log.e("压缩模式=${CompressedMode.valueOf(compressedMode)}")
        Log.e("压缩率=$compressionRatio")
        val images = mAdapter.data.filter { SupportFormat.isSupport(it.originalPath) }

        if (images.isEmpty()) {
            SnackbarUtils.showText(this, getString(R.string.no_image_to_be_compressed))
            return
        }
        if (mAdapter.data.size > images.size)
            SnackbarUtils.showText(
                this,
                "${getString(R.string.contains_unsupported_formats)},${getString(R.string.skip_unsupported_formats)}"
            )
        if (loadingDialog == null)
            loadingDialog = AlertDialog.Builder(this)
                .setView(layoutInflater.inflate(R.layout.dialog_progress, null))
                .setCancelable(false).create()
        loadingDialog?.show()

        CompressorUtils.Builder(this)
            .OnCompressorBefore(object : CompressorUtils.OnCompressorBeforeListener {
                override fun onCompressorBefore(position: Int, images: List<TImage>) {
                    SnackbarUtils.showText(
                        this@MainActivity,
                        String.format(
                            resources.getString(R.string.compression_progress),
                            position + 1,
                            images.size - 1 - position
                        )
                    )
                }
            })
            .buileder()
            .subscribe(object : Observer<CompressorUtils>() {
                override fun onNext(compressorUtils: CompressorUtils) {
                    compressorUtils.run {
                        when (CompressedMode.valueOf(compressedMode)) {
                            CompressedMode.SIZE -> {
                                this.byLuban(images)
                            }
                            CompressedMode.QUALITY -> {
                                this.byNativeTurbo(images, quality = compressionRatio)
                            }
                            CompressedMode.SIZE_QUALITY -> {
                                this.byLuban(images)
                            }
                        }
                    }
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<List<TImage>>() {
                            override fun onNext(list: List<TImage>) {
                                val intent = Intent(this@MainActivity, ImagePreviewActivity::class.java)
                                intent.putExtra(Const.ImageKey, Gson().toJson(list))
                                loadingDialog?.dismiss()
                                startActivity(intent)
                            }
                        })
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_setting -> {
                startActivity(Intent(this, SettingActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 返回键拦截
     */
    override fun onBackPressed() {
        //与上次点击返回键时刻作差
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            //大于2000ms则认为是误操作，使用Snackbar进行提示
            SnackbarUtils.showText(this, getString(R.string.pressAgainToExitTheProgram))
            //并记录下本次点击“返回键”的时刻，以便下次进行判断
            mExitTime = System.currentTimeMillis()
            return
        } else {
            //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
            System.exit(0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        takePhoto.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        takePhoto.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    @SuppressLint("CheckResult")
    override fun takeSuccess(result: TResult?) {
        Observable.fromIterable(result?.images)
            .compose(RxLifecycleUtils.bindToLifecycle(this))
            .subscribe { item ->
                mAdapter.addData(item)
                if (item.fromType == TImage.FromType.CAMERA)//如果是拍照得到的照片则通知系统媒体库更新
                    scanFile(item.originalPath)
                Log.e(item.originalPath)
            }
    }

    override fun takeCancel() {
    }

    override fun takeFail(result: TResult?, msg: String?) {
    }

    override fun invoke(invokeParam: InvokeParam?): PermissionManager.TPermissionType {
        return PermissionManager.TPermissionType.NOT_NEED
    }

    /**
     * 通知媒体库更新文件
     * @param filePath 文件全路径
     */
    private fun scanFile(filePath: String) {
        val scanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        scanIntent.data = Uri.fromFile(File(filePath))
        sendBroadcast(scanIntent)
    }
}