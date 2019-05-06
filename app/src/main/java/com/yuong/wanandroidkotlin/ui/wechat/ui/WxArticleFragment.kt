package com.yuong.wanandroidkotlin.ui.wechat.ui

import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.base.BaseFragment
import com.yuong.wanandroidkotlin.databinding.FragmentWxArticleBinding
import com.yuong.wanandroidkotlin.ui.wechat.viewModel.WxArticleViewModel

class WxArticleFragment : BaseFragment<FragmentWxArticleBinding, WxArticleViewModel>() {

    override fun loadData() {
        viewModel.initNet()
    }

    override fun initView() {
        val bundle = arguments!!
        viewModel.id = bundle.getInt("id")
        viewModel.initUi()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_wx_article
    }

}