package com.yuong.wanandroidkotlin.ui.search.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.base.BaseActivity
import com.yuong.wanandroidkotlin.databinding.ActivitySearchResultBinding
import com.yuong.wanandroidkotlin.livedatabus.CollectEvent
import com.yuong.wanandroidkotlin.livedatabus.LiveDataBus
import com.yuong.wanandroidkotlin.ui.search.viewModel.SearchResultViewModel
import kotlinx.android.synthetic.main.title_bar.*

class SearchResultActivity : BaseActivity<ActivitySearchResultBinding, SearchResultViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeMsg()
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_search_result
    }

    override fun initData() {
        binding!!.viewModel = viewModle
        iv_back.visibility = View.VISIBLE
        iv_back.setOnClickListener { view ->
            finish()
        }
        tv_title.text = intent.getStringExtra("keyWord")
        iv_search.visibility = View.GONE
        viewModle!!.keyWord = intent.getStringExtra("keyWord")
        viewModle!!.initUi()
    }

    fun subscribeMsg() {
        LiveDataBus
                .get()
                .with("collect", CollectEvent::class.java)
                .observe(this, Observer<CollectEvent> { t ->
                    if (t!!.isCollect!!) {
                        viewModle!!.collectArticle(t.id!!, t.position!!)
                    } else {
                        viewModle!!.cancelCollectArticle(t.id!!, t.position!!)
                    }
                })

    }
}