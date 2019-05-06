package com.yuong.wanandroidkotlin.ui.search.ui

import android.view.View
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.base.BaseActivity
import com.yuong.wanandroidkotlin.databinding.ActivitySearchBinding
import com.yuong.wanandroidkotlin.ui.search.viewModel.SearchViewModel
import kotlinx.android.synthetic.main.title_bar.*

class SearchActivity: BaseActivity<ActivitySearchBinding, SearchViewModel>() {

    override fun getLayoutResource(): Int {
        return R.layout.activity_search
    }

    override fun initData() {
        binding!!.viewModel = viewModle
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener { view ->
            finish()
        }
        tv_title.text = "搜索"
        iv_search.visibility = View.GONE
        viewModle!!.initUi()
    }
}