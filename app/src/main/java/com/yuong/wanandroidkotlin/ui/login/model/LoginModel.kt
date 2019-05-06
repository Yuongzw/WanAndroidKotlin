package com.yuong.wanandroidkotlin.ui.login.model

import com.yuong.wanandroidkotlin.net.BaseLoadListener

interface LoginModel {
    /**
     * 登录
     * @param username
     * @param password
     * @param loadListener
     */
    abstract fun login(username: String, password: String, loadListener: BaseLoadListener<Any>)

    /**
     * 注册
     * @param username
     * @param password
     * @param repassword
     * @param loadListener
     */
    abstract fun register(username: String, password: String, repassword: String, loadListener: BaseLoadListener<Any>)


    abstract fun cancelLogin()
}