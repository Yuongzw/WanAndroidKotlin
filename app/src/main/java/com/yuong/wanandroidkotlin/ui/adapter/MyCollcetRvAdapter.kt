package com.yuong.wanandroidkotlin.ui.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.thefinestartist.finestwebview.FinestWebView
import com.yuong.wanandroidkotlin.MyConstans
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.bean.HomeBean
import com.yuong.wanandroidkotlin.databinding.CollectItemBinding
import com.yuong.wanandroidkotlin.livedatabus.CollectEvent
import com.yuong.wanandroidkotlin.livedatabus.LiveDataBus

class MyCollcetRvAdapter(context: Context) : BaseQuickAdapter<HomeBean.DataBean.DatasBean, BaseViewHolder>(null) {
    private var context: Context

    init {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<CollectItemBinding>(LayoutInflater.from(context), R.layout.collect_item, parent, false)
        return MyBaseViewHolder(binding.root)
    }

    override fun convert(helper: BaseViewHolder?, item: HomeBean.DataBean.DatasBean?) {
        val binding = DataBindingUtil.getBinding<CollectItemBinding>(helper!!.itemView)
        binding!!.datasBean = item
        binding.cardView.setOnClickListener { view ->
            FinestWebView.Builder(context).show(item!!.link!!)//点击条目到文章详情页面
        }
        binding.bottomLayout.setOnClickListener { view ->
            val collectEvent = CollectEvent()
            collectEvent.position = helper.position
            collectEvent.isCollect = false
            collectEvent.currentPage = item!!.currentPage
            collectEvent.id = item.id
            collectEvent.originId = item.originId
            LiveDataBus.get().with("unCollect").value = collectEvent
        }

    }

    inner class MyBaseViewHolder(view: View?) : BaseViewHolder(view)
}