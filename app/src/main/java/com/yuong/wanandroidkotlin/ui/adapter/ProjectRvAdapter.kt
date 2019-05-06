package com.yuong.wanandroidkotlin.ui.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.thefinestartist.finestwebview.FinestWebView
import com.yuong.wanandroidkotlin.MyConstans
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.bean.ProjectDetailBean
import com.yuong.wanandroidkotlin.databinding.ProjectDetailItemBinding
import com.yuong.wanandroidkotlin.livedatabus.CollectEvent
import com.yuong.wanandroidkotlin.livedatabus.LiveDataBus
import com.yuong.wanandroidkotlin.util.ToastUtils

class ProjectRvAdapter(context: Context) : BaseQuickAdapter<ProjectDetailBean.DataBean.DatasBean, BaseViewHolder>(null) {

    private var context: Context
    var collectListener: OnCollectListener? = null

    init {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<ProjectDetailItemBinding>(LayoutInflater.from(context), R.layout.project_detail_item, parent, false)
        return MyBaseViewHolder(binding.root)
    }


    override fun convert(helper: BaseViewHolder?, item: ProjectDetailBean.DataBean.DatasBean?) {
        val binding = DataBindingUtil.getBinding<ProjectDetailItemBinding>(helper!!.itemView)
        binding!!.datasBean = item
        helper.itemView.setOnClickListener { view ->
            FinestWebView.Builder(context).show(item!!.link!!)//点击条目到文章详情页面
        }
        binding.projectCollect.setOnClickListener { view ->
            if (collectListener != null) {
                if (binding.projectCollect.isLiked) {
                    collectListener!!.cancelCollect(item!!.id, helper.position)
                } else{
                    collectListener!!.collect(item!!.id, helper.position)
                }
            }
        }
    }

    inner class MyBaseViewHolder(view: View?) : BaseViewHolder(view)

    interface OnCollectListener {
        fun collect(id: Int, position: Int)

        fun cancelCollect(id: Int, position: Int)
    }
}