package com.yuong.wanandroidkotlin.ui.main.model

import com.yuong.wanandroidkotlin.net.BaseLoadListener

interface MainModel {
    /**
     * 登录
     * @param username
     * @param password
     * @param loadListener
     */
    fun login(username: String, password: String, loadListener: BaseLoadListener<Any>)


    /**
     * 退出登录
     * @param loadListener
     */
    fun logout(loadListener: BaseLoadListener<Any>)

    /**
     * 获取APP版本信息
     */
    fun getAppInfo(apiKey: String, appKey: String, loadListener: BaseLoadListener<Any>)
}