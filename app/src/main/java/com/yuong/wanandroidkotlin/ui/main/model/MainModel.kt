package com.yuong.wanandroidkotlin.ui.main.model

import com.yuong.wanandroidkotlin.net.BaseLoadListener

interface MainModel {
    /**
     * 登录
     * @param username
     * @param password
     * @param loadListener
     */
    abstract fun login(username: String, password: String, loadListener: BaseLoadListener<Any>)


    /**
     * 退出登录
     * @param loadListener
     */
    abstract fun logout(loadListener: BaseLoadListener<Any>)
}