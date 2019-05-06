package com.yuong.wanandroidkotlin.ui.wechat.ui

import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.base.BaseFragment
import com.yuong.wanandroidkotlin.databinding.FragmentWxBinding
import com.yuong.wanandroidkotlin.ui.wechat.viewModel.WxViewModel

class WxFragment: BaseFragment<FragmentWxBinding, WxViewModel>() {

    override fun loadData() {
        viewModel.initNet()
    }

    override fun initView() {
        viewModel.initUi()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_wx
    }
}