package com.yuong.wanandroidkotlin.subscriber


import io.reactivex.disposables.Disposable

/**
 * Description : 连接池
 */

interface IDisposablePool {

    /**
     * Created by XQ Yang on 2017/9/6  17:15.
     * Description : 连接池
     */
    fun addDisposable(disposable: Disposable)

    /**
     * 丢弃连接 在view销毁时调用
     */
    fun clearPool()
}