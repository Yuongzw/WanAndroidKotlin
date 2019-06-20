package com.yuong.wanandroidkotlin.ui.setting.viewModel

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.base.BaseViewModel
import com.yuong.wanandroidkotlin.databinding.ActivitySettingBinding
import com.yuong.wanandroidkotlin.ui.setting.ui.SettingActivity
import com.yuong.wanandroidkotlin.util.DataCleanManager

class SettingViewModel : BaseViewModel<ActivitySettingBinding>() {


    override fun initNet() {

    }

    override fun initUi() {
        binding!!.tvCacheSize.text = DataCleanManager.getCacheSize(activity!!.applicationContext.cacheDir)
        binding!!.llCache.setOnClickListener {
            val dialogBuilder = NiftyDialogBuilder.getInstance(activity)

            dialogBuilder
                    .withTitle("清除缓存")
                    .withTitleColor("#FFFFFF")
                    .withDividerColor("#11000000")
                    .withMessage("确定要清除缓存吗？")
                    .withMessageColor("#FFCFCFCF")
                    .withDialogColor("#AF444D50")
                    .withIcon(activity!!.resources.getDrawable(R.drawable.logo))
                    .withDuration(700)
                    .withEffect(Effectstype.SlideBottom)
                    .withButton1Text("取消")
                    .withButton2Text("确定")
                    .isCancelableOnTouchOutside(false)
                    .setButton1Click({
                        DataCleanManager.cleanInternalCache(activity!!.applicationContext)
                        binding!!.tvCacheSize.text = DataCleanManager.getCacheSize(activity!!.applicationContext.cacheDir)
                        dialogBuilder.dismiss()
                    })
                    .setButton2Click({
                        dialogBuilder.dismiss()
                    })
                    .show()
        }

        binding!!.sbNight.setOnCheckedChangeListener { view, isChecked ->
//            if (isChecked) {
//                (activity as SettingActivity).changeSkin(("skin.apk"))
//            } else{
//                (activity as SettingActivity).changeSkin("skin2.apk")
//            }
        }
    }
}