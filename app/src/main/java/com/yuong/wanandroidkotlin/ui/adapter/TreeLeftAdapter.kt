package com.yuong.wanandroidkotlin.ui.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.bean.TreeBean
import com.yuong.wanandroidkotlin.databinding.TreeLeftItemBinding
import com.yuong.wanandroidkotlin.livedatabus.LiveDataBus

class TreeLeftAdapter(context: Context) : BaseQuickAdapter<TreeBean.DataBean, BaseViewHolder>(null) {
    private var context: Context
    private var position = 0

    init {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<TreeLeftItemBinding>(LayoutInflater.from(context), R.layout.tree_left_item, parent, false)
        return MyBaseViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val binding = DataBindingUtil.getBinding<TreeLeftItemBinding>(holder!!.itemView)
        if (this.position == position) {
            binding!!.tvTreeLeftItem.setTextColor(Color.BLUE)
        } else{
            binding!!.tvTreeLeftItem.setTextColor(Color.BLACK)
        }
    }

    override fun convert(helper: BaseViewHolder?, item: TreeBean.DataBean?) {
        val binding = DataBindingUtil.getBinding<TreeLeftItemBinding>(helper!!.itemView)
        binding!!.dataBean = item
        helper.itemView.setOnClickListener { view ->
            if (this.position != helper.position){
                notifyItemChanged(this.position)
                this.position = helper.position
                notifyItemChanged(this.position)
                LiveDataBus.get().with("tree").value = item
            }
        }

    }

    inner class MyBaseViewHolder(view: View?) : BaseViewHolder(view)
}