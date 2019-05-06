package com.yuong.wanandroidkotlin.ui.todo.ui.activity

import android.os.Bundle
import android.view.View
import com.noober.background.BackgroundLibrary
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.base.BaseActivity
import com.yuong.wanandroidkotlin.databinding.ActivityTodoDetailBinding
import com.yuong.wanandroidkotlin.ui.todo.viewModel.TodoDetailViewModel
import kotlinx.android.synthetic.main.activity_todo_detail.*
import kotlinx.android.synthetic.main.title_bar.*

class TodoDetailActivity: BaseActivity<ActivityTodoDetailBinding, TodoDetailViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        BackgroundLibrary.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_todo_detail
    }

    override fun initData() {
        val mode = intent.getIntExtra("mode", 0)
        viewModle!!.mode = mode
        viewModle!!.id = intent.getIntExtra("id", 0)
        viewModle!!.status = intent.getIntExtra("status", 0)
        viewModle!!.title.set(intent.getStringExtra("title"))
        viewModle!!.content.set(intent.getStringExtra("content"))
        viewModle!!.date.set(intent.getStringExtra("date"))
        if (mode == 0) {
            todo_dialog_title.text = "添加代办清单"
            tv_title.text = "添加代办"
        } else if (mode == 1) {
            todo_dialog_title.text = "修改代办清单"
            tv_title.text = "修改代办"
        } else{
            todo_dialog_title.text = "查看代办清单"
            tv_title.text = "查看代办"
        }
        iv_search.visibility = View.GONE
        iv_back.setOnClickListener { finish() }
        binding!!.viewModel = viewModle
        viewModle!!.initUi()
    }
}