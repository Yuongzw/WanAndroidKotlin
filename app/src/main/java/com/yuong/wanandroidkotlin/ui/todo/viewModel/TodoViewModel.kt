package com.yuong.wanandroidkotlin.ui.todo.viewModel

import android.os.Bundle
import android.support.v4.app.Fragment
import com.yuong.wanandroidkotlin.base.BaseViewModel
import com.yuong.wanandroidkotlin.databinding.ActivityTodoBinding
import com.yuong.wanandroidkotlin.ui.adapter.MyViewPagerAdapter
import com.yuong.wanandroidkotlin.ui.todo.ui.fragment.TodoFragment

class TodoViewModel : BaseViewModel<ActivityTodoBinding>() {
    private var fragments: MutableList<Fragment>? = null
    var todoFragment: TodoFragment? = null
    var todoDoneFragment: TodoFragment? = null
    private lateinit var adapter: MyViewPagerAdapter

    override fun initUi() {
        initFragments()
        adapter = MyViewPagerAdapter(activity!!.supportFragmentManager, fragments!!)
        binding!!.todoVp.adapter = adapter
    }

    override fun initNet() {
    }

    fun initFragments() {
        fragments = ArrayList()
        todoFragment = TodoFragment()
        val bundle1 = Bundle()
        bundle1.putInt("status", 0)
        todoFragment!!.setArguments(bundle1)

        todoDoneFragment = TodoFragment()
        val bundle2 = Bundle()
        bundle2.putInt("status", 1)//传递 ID
        todoDoneFragment!!.setArguments(bundle2)
        (fragments as ArrayList<Fragment>).add(todoFragment!!)
        (fragments as ArrayList<Fragment>).add(todoDoneFragment!!)
    }
}