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
import com.yuong.wanandroidkotlin.databinding.ArticleItemBinding
import com.yuong.wanandroidkotlin.livedatabus.CollectEvent
import com.yuong.wanandroidkotlin.livedatabus.LiveDataBus

class MyRvAdapter(context: Context) : BaseQuickAdapter<HomeBean.DataBean.DatasBean, BaseViewHolder>(null) {
    private var context: Context

    var collectListener: OnCollectListener? = null

    init {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<ArticleItemBinding>(LayoutInflater.from(context), R.layout.article_item, parent, false)
        return MyBaseViewHolder(binding.root)
    }

    override fun convert(helper: BaseViewHolder?, item: HomeBean.DataBean.DatasBean?) {
        val binding = DataBindingUtil.getBinding<ArticleItemBinding>(helper!!.itemView)
        binding!!.datasBean = item
        helper.itemView.setOnClickListener { view ->
            FinestWebView.Builder(context).show(item!!.link!!)//点击条目到文章详情页面
        }
        binding.likeButton.setOnClickListener { view ->
            if (collectListener != null) {
                if (binding.likeButton.isLiked) {
                    collectListener!!.cancelCollect(item!!.id, helper.position)
                } else{
                    collectListener!!.collect(item!!.id, helper.position)
                }
            }
//            if (MyConstans.ISLOGIN) {
//                val collectEvent = CollectEvent()
//                collectEvent.position = helper.position
//                collectEvent.isCollect = !binding.likeButton.isLiked
//                collectEvent.currentPage = item!!.currentPage
//                collectEvent.id = item!!.id
//                LiveDataBus.get().with("collect").value = collectEvent
//            } else {
//
//            }
        }
    }

    inner class MyBaseViewHolder(view: View?) : BaseViewHolder(view)

    interface OnCollectListener {
        fun collect(id: Int, position: Int)

        fun cancelCollect(id: Int, position: Int)
    }
}