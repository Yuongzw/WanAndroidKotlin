package com.yuong.wanandroidkotlin.ui.home.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.base.BaseFragment
import com.yuong.wanandroidkotlin.databinding.FragmentHomeBinding
import com.yuong.wanandroidkotlin.livedatabus.CollectEvent
import com.yuong.wanandroidkotlin.livedatabus.LiveDataBus
import com.yuong.wanandroidkotlin.ui.home.viewModel.HomeViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override fun loadData() {
        viewModel.initNet()
    }

    override fun initView() {
        viewModel.initUi()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_home
    }

}