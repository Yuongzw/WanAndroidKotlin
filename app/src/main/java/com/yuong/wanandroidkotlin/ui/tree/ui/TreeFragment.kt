package com.yuong.wanandroidkotlin.ui.tree.ui

import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.base.BaseFragment
import com.yuong.wanandroidkotlin.databinding.FragmentTreeBinding
import com.yuong.wanandroidkotlin.ui.tree.viewModel.TreeViewModel

class TreeFragment: BaseFragment<FragmentTreeBinding, TreeViewModel>() {

    override fun loadData() {
        viewModel.initNet()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_tree
    }

    override fun initView() {
        viewModel.initUi()
    }

}