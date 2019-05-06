package com.yuong.wanandroidkotlin

import java.util.ArrayList

class MyConstans {
    companion object {
        val BASE_URL = "https://www.wanandroid.com/"
        val HOME_NEWS_TYPE = "selected"
        val HOME_NEWS_DISCOVERY = "discovery"
        val HOME_NEWS_ATTENTION = "follow"
        var collectIds: MutableList<Int> = ArrayList()
        var ISLOGIN = false


        /*网络错误*/
        val NETWORD_ERROR = 0x1
        /*fastjson错误*/
        val JSON_ERROR = 0x3
        /*未知错误*/
        val UNKNOWN_ERROR = 0x4

        val CONNECT_ERROR = 0x7

        val ORDER_ERROR = 0x8

        val AUTH_ERROR = 0x9

        val ERROR404 = 0x10
    }
}