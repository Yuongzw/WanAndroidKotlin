package com.yuong.wanandroidkotlin.ui.todo.ui.activity

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.base.BaseActivity
import com.yuong.wanandroidkotlin.bean.TodoListBean
import com.yuong.wanandroidkotlin.databinding.ActivityTodoBinding
import com.yuong.wanandroidkotlin.livedatabus.LiveDataBus
import com.yuong.wanandroidkotlin.ui.todo.viewModel.TodoViewModel
import kotlinx.android.synthetic.main.title_bar.*

class TodoActivity: BaseActivity<ActivityTodoBinding, TodoViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeMsg()
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_todo
    }

    override fun initData() {
        tv_title.text = "我的代办"
        iv_search.visibility = View.GONE
        iv_back.setOnClickListener { view -> finish() }
        viewModle!!.initUi()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == 1 || requestCode == 0) && resultCode == RESULT_OK) { //修改或者添加返回
            viewModle!!.todoFragment!!.viewModel.requestNet()
        }
    }


    fun subscribeMsg() {
        LiveDataBus
                .get()
                .with("updateTodo", TodoListBean.DataBean.DatasBean::class.java)
                .observe(this, Observer { t ->
                    if (t!!.status == 0) { //完成
                        t.status = 1
                        viewModle!!.todoDoneFragment!!.viewModel.addTodo(t)
                    } else {    //未完成
                        t.status = 0
                        viewModle!!.todoFragment!!.viewModel.addTodo(t)
                    }
                })

    }
}