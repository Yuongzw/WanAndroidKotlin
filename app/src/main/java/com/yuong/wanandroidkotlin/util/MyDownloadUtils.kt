package com.yuong.wanandroidkotlin.util

import android.text.TextUtils
import android.util.Log
import com.yuong.wanandroidkotlin.MyConstans

import java.io.File
import java.io.IOException
import java.io.RandomAccessFile

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class MyDownloadUtils private constructor() {
    private var client: OkHttpClient? = null
    private var mlistener: DownloadListener? = null
    private var file: File? = null
    private var fileAbsolutePath: String? = null
    var downloadFile: File? = null
    private var startPosition: Long = 0
    private var call: Call? = null

    init {
        client = OkHttpClient()
    }

    fun setListener(listener: DownloadListener) {
        this.mlistener = listener
    }

    /**
     * 初始化下载父路径
     *
     * @return
     */
    fun initDownload(path: String) {
        file = File(path)
        if (!file!!.parentFile.exists()) {
            file!!.parentFile.mkdir()
        }
        if (!file!!.exists()) {
            file!!.mkdir()
        }
        //        if(!file.exists()) {
        //            try {
        //                file.createNewFile();
        //            } catch (IOException e) {
        //                e.printStackTrace();
        //            }
        //        }

        fileAbsolutePath = file!!.absolutePath
        Log.d("DownloadUtils", fileAbsolutePath!!.toString())
    }

    fun reset() {
        if (call != null) {
            call!!.cancel()
            call = null
            client = null
        }
        instance = null
    }

    fun startDownload(url: String) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        val fn = MyConstans.APP_NAME
        downloadFile = File(file, fn)
        if (downloadFile!!.exists() && downloadFile!!.isFile) {
            if (downloadFile!!.length() > 1024.0 * 1024.0 * 4.22) {
                mlistener!!.finishDownload()
                return
            }
            Log.e("yuong", downloadFile!!.length().toString() + "........")
        }
        Log.d("DownloadUtils", "downloadFile$downloadFile")
        startPosition = 0
        if (downloadFile!!.exists()) {
            startPosition = downloadFile!!.length()
        }
        val request = Request.Builder()
                .addHeader("RANGE", "bytes=$startPosition-")
                .url(url)
                .build()
        call = client!!.newCall(request)
        call!!.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                mlistener!!.startDownload()
                val body = response.body()
                //                startPosition
                val totalLength = body!!.contentLength()
                Log.d("DownloadUtils", "totalLength: $totalLength----")
                val inputStream = body.byteStream()
                val bytes = ByteArray(2048)
                var len = 0
                var totalNum = startPosition
                val raf = RandomAccessFile(downloadFile, "rw")
                len = inputStream.read(bytes, 0, bytes.size)
                while (len != -1) {
                    raf.seek(totalNum)
                    raf.write(bytes, 0, len)
                    totalNum += len.toLong()
                    mlistener!!.downloadProgress(totalNum * 100 / totalLength)
                    len = inputStream.read(bytes, 0, bytes.size)
                }
                mlistener!!.finishDownload()
                body.close()
            }
        })
    }

    fun stopDownload() {
        mlistener!!.startDownload()
        if (call != null && call!!.isExecuted) {
            call!!.cancel()
        }
    }

    companion object {
//        @Volatile
//        private var instance: MyDownloadUtils? = null
//
//        fun getInstance(): MyDownloadUtils? {
//            if (instance == null) {
//                synchronized(MyDownloadUtils::class.java) {
//                    if (instance == null) {
//                        instance = MyDownloadUtils()
//                    }
//                }
//            }
//            return instance
//        }

        @Volatile
        private var instance: MyDownloadUtils? = null
            /*获取单例*/
            get() {
                if (field == null) {
                    field = MyDownloadUtils()
                }
                return field
            }

        //第一种单例
        @Synchronized
        fun get(): MyDownloadUtils {
            //细心的小伙伴肯定发现了，这里不用getInstance作为为方法名，是因为在伴生对象声明时，内部已有getInstance方法，所以只能取其他名字
            return instance!!
        }
    }
}
