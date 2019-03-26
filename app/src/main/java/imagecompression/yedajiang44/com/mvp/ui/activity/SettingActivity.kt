package imagecompression.yedajiang44.com.mvp.ui.activity

import android.Manifest
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.files.folderChooser
import com.tbruyelle.rxpermissions2.RxPermissions
import imagecompression.yedajiang44.com.R
import imagecompression.yedajiang44.com.app.Utils.Const
import imagecompression.yedajiang44.com.app.Utils.SnackbarUtils
import imagecompression.yedajiang44.com.app.base.BaseActivity
import imagecompression.yedajiang44.com.mvp.model.entity.CompressedMode
import kotlinx.android.synthetic.main.activity_setting.*
import org.lzh.framework.updatepluginlib.UpdateBuilder
import org.lzh.framework.updatepluginlib.base.CheckCallback
import org.lzh.framework.updatepluginlib.model.Update


class SettingActivity : BaseActivity() {
    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_setting
    }

    override fun initData(savedInstanceState: Bundle?) {
        initToolbar()
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val settingsFragment = SettingsFragment()
        transaction.add(R.id.preferences, settingsFragment, null)
        transaction.commit()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        private lateinit var compressionRatio: SeekBarPreference
        private lateinit var compressedMode: ListPreference
        private lateinit var chooseFolder: Preference
        override fun onCreatePreferences(p0: Bundle?, p1: String?) {
            preferenceManager.sharedPreferencesName = Const.SettingsSharedPreferencesName//setting就是修改后的配置名
            addPreferencesFromResource(R.xml.preferences)//必须放在修改配置名之后
            compressionRatio = findPreference(resources.getString(R.string.compression_ratio_key)) as SeekBarPreference
            compressedMode = findPreference(resources.getString(R.string.compressed_mode_key)) as ListPreference
            chooseFolder = findPreference(resources.getString(R.string.choose_out_file_path_key))
            updateCompressionRatioState(compressedMode.value)
            chooseFolder.summary = preferenceManager.sharedPreferences.getString(chooseFolder.key, null) ?:
                    context?.obbDir?.absolutePath
            compressedMode.onPreferenceChangeListener =
                    Preference.OnPreferenceChangeListener { _, newValue ->
                        updateCompressionRatioState(newValue as String)
                        return@OnPreferenceChangeListener true
                    }
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            when (preference?.key) {
                chooseFolder.key -> {
                    RxPermissions(activity!!)
                        .request(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        .subscribe { permission ->
                            if (permission) {
                                MaterialDialog(context!!)
                                    .title(R.string.choose_file_path)
                                    .folderChooser(
                                        allowFolderCreation = true,
                                        folderCreationLabel = R.string.create_folder, // optional as well
                                        emptyTextRes = R.string.emptyTextRes
                                    ) { dialog, folder ->
                                        chooseFolder.summary = folder.absolutePath
                                        preferenceManager.sharedPreferences.edit()
                                            .putString(chooseFolder.key, folder.absolutePath).apply()
                                    }
                                    .show()
                            } else {
                                SnackbarUtils.showText(activity!!, resources.getString(R.string.noRelevantPermissions))
                            }
                        }
                }
                resources.getString(R.string.check_update_key) -> {
                    UpdateBuilder.create().setCheckCallback(object : CheckCallback {
                        override fun onCheckStart() {
                            SnackbarUtils.showText(activity!!, resources.getString(R.string.checking_update))
                        }

                        override fun onUserCancel() {
                        }

                        override fun hasUpdate(update: Update?) {
                        }

                        override fun onCheckIgnore(update: Update?) {
                        }

                        override fun noUpdate() {
                            SnackbarUtils.showText(activity!!, resources.getString(R.string.noUpdate))
                        }

                        override fun onCheckError(t: Throwable?) {
                            SnackbarUtils.showText(activity!!, resources.getString(R.string.noUpdate))
                        }
                    }).check()
                }
            }
            return super.onPreferenceTreeClick(preference)
        }

        /**
         * 更新压缩率启用状态
         * @param compressedMode 压缩模式
         */
        private fun updateCompressionRatioState(compressedMode: String) {
            compressionRatio.isEnabled = compressedMode == CompressedMode.QUALITY.name
        }
    }
}
