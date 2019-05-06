package com.yuong.wanandroidkotlin.net

interface BaseLoadListener<T> {
    /**
     * 加载数据成功
     *
     */
    abstract fun loadSuccess(t: T)

    /**
     * 加载失败
     *
     * @param message
     */
    abstract fun loadFailure(message: String)

    /**
     * 开始加载
     */
    abstract fun loadStart()

    /**
     * 加载结束
     */
    abstract fun loadComplete()
}