package com.yuong.wanandroidkotlin.ui.adapter

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.bean.TreeBean
import com.yuong.wanandroidkotlin.databinding.TreeRightItemBinding
import com.yuong.wanandroidkotlin.ui.tree.ui.TreeArticleActivity
import com.yuong.wanandroidkotlin.util.ToastUtils

class TreeRightAdapter(context: Context) : BaseQuickAdapter<TreeBean.DataBean.ChildrenBean, BaseViewHolder>(null) {
    private var context: Context

    init {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<TreeRightItemBinding>(LayoutInflater.from(context), R.layout.tree_right_item, parent, false)
        return MyBaseViewHolder(binding.root)
    }

    override fun convert(helper: BaseViewHolder?, item: TreeBean.DataBean.ChildrenBean?) {
        val binding = DataBindingUtil.getBinding<TreeRightItemBinding>(helper!!.itemView)
        binding!!.dataBean = item
        helper.itemView.setOnClickListener { view ->
            val intent = Intent(context, TreeArticleActivity::class.java)
            intent.putExtra("cid", item!!.id)
            intent.putExtra("title", item.name)
            context.startActivity(intent)
            ToastUtils.showShort(item!!.name + "  " + item.id)
        }

    }

    inner class MyBaseViewHolder(view: View?) : BaseViewHolder(view)
}