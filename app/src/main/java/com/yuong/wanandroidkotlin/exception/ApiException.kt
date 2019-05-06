package com.yuong.wanandroidkotlin.exception

/**
 * Created by liukun on 16/3/10.
 */
class ApiException : Exception {

    /*错误码*/
    var code: Int = 0
    /*显示的信息*/
    var displayMessage: String? = null

    constructor(e: Throwable) : super(e) {}

    constructor(cause: Throwable, code: Int, showMsg: String) : super(showMsg, cause) {
        this.code = code
        displayMessage = showMsg
    }
}

