package com.yuong.wanandroidkotlin.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.bumptech.glide.Glide
import com.squareup.leakcanary.LeakCanary
import com.tencent.bugly.Bugly
import com.tinkerpatch.sdk.TinkerPatch
import com.tinkerpatch.sdk.loader.TinkerPatchApplicationLike
import com.yuong.wanandroidkotlin.skin.SkinEngine
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
        //初始化换肤引擎
        SkinEngine.instance.init(this)
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(instance)
        Thread(Runnable {
            // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
            // 调试时，将第三个参数改为true
            Bugly.init(context, "a9fa1e3f68", true)

        }).start()
        // 我们可以从这里获得Tinker加载过程的信息
        val tinkerApplicationLike = TinkerPatchApplicationLike.getTinkerPatchApplicationLike();

        // 初始化TinkerPatch SDK, 更多配置可参照API章节中的,初始化SDK
        TinkerPatch.init(tinkerApplicationLike)
                .reflectPatchLibrary()
                .setPatchRollbackOnScreenOff(true)
                .setPatchRestartOnSrceenOff(true)
                .setFetchPatchIntervalByHours(1)

        // 每隔3个小时(通过setFetchPatchIntervalByHours设置)去访问后台时候有更新,通过handler实现轮训的效果
        TinkerPatch.with().fetchPatchUpdateAndPollWithInterval()

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