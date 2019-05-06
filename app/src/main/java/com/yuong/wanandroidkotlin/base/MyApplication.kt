package com.yuong.wanandroidkotlin.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import com.bumptech.glide.Glide
import com.squareup.leakcanary.LeakCanary
import com.yuong.wanandroidkotlin.util.ToastUtils

class MyApplication : Application() {
    companion object {
        var instance: Application? = null
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        instance = this
        ToastUtils.init(context!!)
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(instance)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if(level == TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory()
        }
        Glide.get(this).trimMemory(level)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).clearMemory()
    }

}