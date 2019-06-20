package com.yuong.wanandroidkotlin

import android.os.Environment
import java.io.File
import java.util.ArrayList

class MyConstans {
    companion object {
        val BASE_URL = "https://www.wanandroid.com/"
        val HOME_NEWS_TYPE = "selected"
        val HOME_NEWS_DISCOVERY = "discovery"
        val HOME_NEWS_ATTENTION = "follow"
        var collectIds: MutableList<Int> = ArrayList()
        var ISLOGIN = false

        //蒲公英平台
        val API_Key = "f74d5371f03f216aa95ffaa5376c91c2"
        val User_Key = "53bef6e0a9f351d590210f6f0e74a757"
        val App_Key = "9429bd85fa559aa6bcdfb82568d2e52b"
        val APP_NAME = "玩安卓APP.apk"

        // 默认存放文件下载的路径
        val DEFAULT_SAVE_FILE_PATH = (Environment.getExternalStorageDirectory().toString()
                + File.separator
                + "wan_android"
                + File.separator + "download" + File.separator)


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