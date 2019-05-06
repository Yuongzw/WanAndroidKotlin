package com.yuong.wanandroidkotlin.exception

/**
 * Created by liukun on 16/3/10.
 */
class ErrorOrderException : RuntimeException {
    /**
     * Creates exception with the specified message. If you are wrapping another exception, consider
     * using [.ErrorOrderException] instead.
     *
     * @param msg error message describing a possible cause of this exception.
     */
    constructor(msg: String) : super(msg) {}

    /**
     * Creates exception with the specified message and cause.
     *
     * @param msg error message describing what happened.
     * @param cause root exception that caused this exception to be thrown.
     */
    constructor(msg: String, cause: Throwable) : super(msg, cause) {}

    /**
     * Creates exception with the specified cause. Consider using
     * [.ErrorOrderException] instead if you can describe what happened.
     *
     * @param cause root exception that caused this exception to be thrown.
     */
    constructor(cause: Throwable) : super(cause) {}

    companion object {
        internal val serialVersionUID = -4086729973971783390L
    }
}

