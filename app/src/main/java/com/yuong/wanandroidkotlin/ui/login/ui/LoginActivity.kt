package com.yuong.wanandroidkotlin.ui.login.ui

import android.os.Bundle
import com.noober.background.BackgroundLibrary
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.base.BaseActivity
import com.yuong.wanandroidkotlin.databinding.ActivityLoginBinding
import com.yuong.wanandroidkotlin.ui.login.viewModel.LoginViewModel

class LoginActivity: BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        BackgroundLibrary.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_login
    }

    override fun initData() {
        binding!!.viewModel = viewModle
        viewModle!!.initUi()
        viewModle!!.initNet()
    }
}