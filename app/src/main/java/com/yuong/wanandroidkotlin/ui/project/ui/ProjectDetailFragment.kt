package com.yuong.wanandroidkotlin.ui.project.ui

import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.base.BaseFragment
import com.yuong.wanandroidkotlin.databinding.FragmentProjectDetailBinding
import com.yuong.wanandroidkotlin.ui.project.viewModel.ProjectDetailViewModel

class ProjectDetailFragment : BaseFragment<FragmentProjectDetailBinding, ProjectDetailViewModel>() {

    override fun initView() {
        viewModel.initUi()
    }

    override fun loadData() {
        val bundle = arguments!!
        viewModel.cid = bundle.getInt("id")
        viewModel.initNet()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_project_detail
    }

}