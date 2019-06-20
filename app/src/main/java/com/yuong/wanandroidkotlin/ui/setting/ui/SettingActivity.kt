package com.yuong.wanandroidkotlin.ui.setting.ui

import android.os.Bundle
import android.view.View
import com.noober.background.BackgroundLibrary
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.base.BaseActivity
import com.yuong.wanandroidkotlin.databinding.ActivitySettingBinding
import com.yuong.wanandroidkotlin.ui.setting.viewModel.SettingViewModel
import kotlinx.android.synthetic.main.title_bar.*

class SettingActivity: BaseActivity<ActivitySettingBinding, SettingViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        BackgroundLibrary.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_setting
    }

    override fun initData() {
        iv_search.visibility = View.GONE
        iv_back.visibility = View.VISIBLE
        tv_title.text = "设置"
        iv_back.setOnClickListener { finish() }
        viewModle!!.initUi()
    }
}