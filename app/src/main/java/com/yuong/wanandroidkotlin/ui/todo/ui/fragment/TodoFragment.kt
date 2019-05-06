package com.yuong.wanandroidkotlin.ui.todo.ui.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.base.BaseFragment
import com.yuong.wanandroidkotlin.bean.TodoListBean
import com.yuong.wanandroidkotlin.databinding.FragmentTodoBinding
import com.yuong.wanandroidkotlin.livedatabus.CollectEvent
import com.yuong.wanandroidkotlin.livedatabus.LiveDataBus
import com.yuong.wanandroidkotlin.ui.todo.viewModel.TodoFragmentViewModel



class TodoFragment: BaseFragment<FragmentTodoBinding, TodoFragmentViewModel>() {



    override fun initView() {
        val bundle = getArguments()
        val status = bundle!!.getInt("status")
        viewModel.status = status
        binding.viewModel = viewModel
        viewModel.initUi()
    }

    override fun loadData() {
        viewModel.initNet()
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_todo
    }

}