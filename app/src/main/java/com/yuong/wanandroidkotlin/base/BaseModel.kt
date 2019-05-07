package com.yuong.wanandroidkotlin.base

import android.content.Context
import com.yuong.wanandroidkotlin.net.MyRetrofit
import com.yuong.wanandroidkotlin.net.NetApi
import com.yuong.wanandroidkotlin.subscriber.DefaultDisposablePoolImpl

open class BaseModel(context: Context) : DefaultDisposablePoolImpl() {

    protected var api: NetApi
    init {
        api = MyRetrofit.get().api!!
    }
}