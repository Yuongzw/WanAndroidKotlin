package com.yuong.wanandroidkotlin.subscriber


import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Description :实现了连接丢弃
 *
 * @date 2018/6/19  13:45
 */
open class DefaultDisposablePoolImpl : IDisposablePool {
    private var mDisposable: CompositeDisposable? = null

    override fun addDisposable(disposable: Disposable) {
        if (mDisposable == null) {
            mDisposable = CompositeDisposable(disposable)
        } else {
            mDisposable!!.add(disposable)
        }
    }

    fun deleteDisposable(disposable: Disposable) {
        mDisposable!!.delete(disposable)
    }

    override fun clearPool() {
        if (mDisposable != null) {
            mDisposable!!.clear()
            mDisposable = null
        }
    }
}