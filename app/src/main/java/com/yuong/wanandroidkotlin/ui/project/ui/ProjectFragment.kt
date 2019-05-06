package com.yuong.wanandroidkotlin.ui.project.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.view.ViewPager
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.base.BaseFragment
import com.yuong.wanandroidkotlin.databinding.FragmentProjectBinding
import com.yuong.wanandroidkotlin.livedatabus.CollectEvent
import com.yuong.wanandroidkotlin.livedatabus.LiveDataBus
import com.yuong.wanandroidkotlin.ui.project.viewModel.ProjectViewModel
import kotlinx.android.synthetic.main.fragment_project.*

class ProjectFragment : BaseFragment<FragmentProjectBinding, ProjectViewModel>() {

    override fun initView() {
        viewModel.initUi()

    }

    override fun loadData() {
        viewModel.initNet()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_project
    }

}