package com.yuong.wanandroidkotlin.ui.tree.ui

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.base.BaseActivity
import com.yuong.wanandroidkotlin.databinding.ActivityTreeArticleBinding
import com.yuong.wanandroidkotlin.livedatabus.CollectEvent
import com.yuong.wanandroidkotlin.livedatabus.LiveDataBus
import com.yuong.wanandroidkotlin.ui.search.ui.SearchActivity
import com.yuong.wanandroidkotlin.ui.tree.viewModel.TreeArticleViewModel
import kotlinx.android.synthetic.main.title_bar.*

class TreeArticleActivity: BaseActivity<ActivityTreeArticleBinding, TreeArticleViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeMsg()
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_tree_article
    }

    override fun initData() {
        tv_title.setText(intent.getStringExtra("title"))
        iv_back.setOnClickListener { view -> finish() }
        iv_search.setOnClickListener { view ->
            startActivity(Intent(this, SearchActivity::class.java))
        }
        val cid = intent.getIntExtra("cid", 0)
        viewModle!!.cid = cid
        viewModle!!.initUi()
    }

    fun subscribeMsg() {
        LiveDataBus
                .get()
                .with("collect", CollectEvent::class.java)
                .observe(this, Observer { t ->
                    if (t!!.currentPage == 1) {
                        if (t.isCollect!!) {
                            viewModle!!.collectArticle(t.id!!, t.position!!)
                        } else {
                            viewModle!!.cancelCollectArticle(t.id!!, t.position!!)
                        }
                    }
                })

    }
}