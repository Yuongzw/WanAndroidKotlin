package com.yuong.wanandroidkotlin.base

import android.content.Context
import android.databinding.ViewDataBinding
import android.support.v4.app.FragmentActivity

abstract class BaseViewModel<V : ViewDataBinding> {
    var binding: V? = null
    var activity: FragmentActivity? = null
    var context:Context? = null

    fun init(activity: FragmentActivity, binding: V, context:Context){
        this.activity = activity
        this.binding = binding
        this.context = context
    }

    abstract fun initUi()

    abstract fun initNet()

    fun destroy() {
        if (context != null) {
            context = null
        }
        if (activity != null) {
            activity = null
        }
    }
}