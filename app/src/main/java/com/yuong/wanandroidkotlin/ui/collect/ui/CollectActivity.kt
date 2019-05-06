package com.yuong.wanandroidkotlin.ui.collect.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.base.BaseActivity
import com.yuong.wanandroidkotlin.databinding.ActivityCollectBinding
import com.yuong.wanandroidkotlin.livedatabus.CollectEvent
import com.yuong.wanandroidkotlin.livedatabus.LiveDataBus
import com.yuong.wanandroidkotlin.ui.collect.viewModel.CollectViewModel
import kotlinx.android.synthetic.main.title_bar.*

class CollectActivity: BaseActivity<ActivityCollectBinding, CollectViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeMsg()
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_collect
    }

    override fun initData() {
        tv_title.text = "我的收藏"
        iv_back.visibility = View.VISIBLE
        iv_search.visibility = View.GONE
        iv_back.setOnClickListener { view -> finish() }
        viewModle!!.initUi()
    }

    fun subscribeMsg() {
        LiveDataBus
                .get()
                .with("unCollect", CollectEvent::class.java)
                .observe(this, Observer<CollectEvent> { t ->
                   viewModle!!.cancelCollectArticle(t!!.id!!, t.originId!!)
                })

    }
}