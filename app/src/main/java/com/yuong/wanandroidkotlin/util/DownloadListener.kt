package com.yuong.wanandroidkotlin.util

interface DownloadListener {
    fun startDownload()

    fun stopDownload()

    fun finishDownload()

    fun downloadProgress(progress: Long)
}

