package com.yuong.wanandroidkotlin.ui.adapter

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.bean.TodoListBean
import com.yuong.wanandroidkotlin.databinding.TodoChildItemBinding
import com.yuong.wanandroidkotlin.databinding.TodoGroupItemBinding

class MyTodoAdapter(groupList: MutableList<String>, childTodoList: MutableList<MutableList<TodoListBean.DataBean.DatasBean>>) : BaseExpandableListAdapter() {
    private var groupList: MutableList<String>
    private var childTodoList: MutableList<MutableList<TodoListBean.DataBean.DatasBean>>
    var onTodoClickListener: OnTodoClickListener? = null

    init {
        this.groupList = groupList
        this.childTodoList = childTodoList
    }


    override fun getGroup(groupPosition: Int): Any {
        return groupPosition
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val groupItemBinding: TodoGroupItemBinding
        val view: View
        if (convertView == null) {
            groupItemBinding =  DataBindingUtil.inflate(LayoutInflater.from(parent!!.getContext()), R.layout.todo_group_item, parent, false)
        }else{
            groupItemBinding = DataBindingUtil.getBinding(convertView)!!
        }
        view = groupItemBinding.root
        groupItemBinding.value = groupList.get(groupPosition)
        return view
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return childTodoList[groupPosition].size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return childTodoList.get(groupPosition).get(childPosition)
    }

    override fun getGroupId(getGroupId: Int): Long {
        return getGroupId.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, view: View?, viewGroup: ViewGroup?): View {
        val childItemBinding: TodoChildItemBinding
        val currentView: View
        if (view == null) {
            childItemBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup!!.getContext()), R.layout.todo_child_item, viewGroup, false)

        } else{
            childItemBinding = DataBindingUtil.getBinding(view)!!
        }
        currentView = childItemBinding.root
        childItemBinding.datasBean = childTodoList.get(groupPosition).get(childPosition)
        if (childTodoList[groupPosition][childPosition].content.equals("")) {  //如果内容或者日期为空，隐藏 tv_content_or_finishDate 控件
            childItemBinding.tvContentOrFinishDate.visibility = View.GONE
        } else {    //反之显示 tv_content_or_finishDate 控件 并为其 赋值
            childItemBinding.tvContentOrFinishDate.visibility = View.VISIBLE
        }
        if (childTodoList[groupPosition][childPosition].status == 0) {    //如果是未完成
            childItemBinding.ivCompleteOrCancel.setImageResource(R.drawable.ic_complete) //就取 打钩的照片
        } else {    //如果已完成
            childItemBinding.ivCompleteOrCancel.setImageResource(R.drawable.ic_recovery)//就取恢复的界面
        }
        childItemBinding.ivDelete.setOnClickListener {
            if (onTodoClickListener != null) {
                onTodoClickListener!!.deleteTodo(groupPosition, childPosition)
            }
        }
        childItemBinding.ivCompleteOrCancel.setOnClickListener {
            if (onTodoClickListener != null) {
                onTodoClickListener!!.updateTodo(groupPosition, childPosition)
            }
        }
        childItemBinding.tvTodoTitle.setOnClickListener {
            if (onTodoClickListener != null) {
                onTodoClickListener!!.itemClick(groupPosition, childPosition)
            }
        }
        return currentView
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return groupList.size
    }

    interface OnTodoClickListener {
        fun deleteTodo(groupPosition: Int, childPosition: Int)

        fun updateTodo(groupPosition: Int, childPosition: Int)

        fun itemClick(groupPosition: Int, childPosition: Int)
    }
}