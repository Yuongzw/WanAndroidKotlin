package com.yuong.wanandroidkotlin.net

import android.content.Context
import android.util.Log
import com.yuong.wanandroidkotlin.MyConstans
import com.yuong.wanandroidkotlin.base.MyApplication
import com.yuong.wanandroidkotlin.util.NetWorkUtil
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.HashSet
import java.util.concurrent.TimeUnit

open class MyRetrofit {
    companion object {
        /*单例*/
        @Volatile
        private var instance: MyRetrofit? = null
            /*获取单例*/
            get() {
                if (field == null) {
                    field = MyRetrofit()
                }
                return field
            }
        //第一种单例
        @Synchronized
        fun get(): MyRetrofit {
            //细心的小伙伴肯定发现了，这里不用getInstance作为为方法名，是因为在伴生对象声明时，内部已有getInstance方法，所以只能取其他名字
            return instance!!
        }

        //第二种单例
        fun getInstance(context: Context): MyRetrofit {
            synchronized(context) {
                if (instance == null) {
                    instance = MyRetrofit()
                }
                return instance!!
            }
        }
    }

    var api: NetApi? = null


    private val cookies = HashSet<String>()

    private constructor() {
        //设置缓存路径 内置存储
        //File httpCacheDirectory = new File(context.getCacheDir(), "responses");
        //外部存储
        val httpCacheDirectory = File(MyApplication.context!!.cacheDir, "responses")
        //设置缓存 10M
        val cacheSize: Long = 10 * 1024 * 1024
        val cache = Cache(httpCacheDirectory, cacheSize)

        val client = OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(AddCookiesInterceptor())
                .addInterceptor(GetCookiesInterceptor())
                .addInterceptor(GetCache())
                .addNetworkInterceptor(WriteCache())
                .connectTimeout(10, TimeUnit.SECONDS).build()
        val retrofit = Retrofit.Builder()
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())////增加返回值为String的支持
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(MyConstans.BASE_URL)
                .build()
        api = retrofit.create(NetApi::class.java)
    }

    constructor(uri: String) {
        //外部存储
        val client = OkHttpClient.Builder()
                .addInterceptor(AddCookiesInterceptor())
                .addInterceptor(GetCookiesInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS).build()
        val retrofit = Retrofit.Builder()
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())////增加返回值为String的支持
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(uri)
                .build()
        api = retrofit.create(NetApi::class.java)
    }


    fun reset() {
        if (instance != null) {
            instance = null
        }
    }

    inner class GetCookiesInterceptor : Interceptor {
        private var headers: MutableList<String>? = null
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalResponse = chain.proceed(chain.request())
            headers = originalResponse.headers("Set-Cookie")
            for (header in headers!!) {
                cookies.add(header)
            }
            return originalResponse
        }

    }

    inner class AddCookiesInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request().newBuilder()
            for (cookie in cookies) {
                builder.addHeader("Cookie", cookie)
                Log.v("OkHttp", "Adding Header: $cookie")
            }
            return chain.proceed(builder.build())
        }

    }

    //获取缓存
    inner class GetCache : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            if (!NetWorkUtil.isNetworkConnected(MyApplication.context)) {
                val maxStale: Int = 60 * 60 * 24 * 28 // 离线时缓存保存4周,单位:秒
                val tempCacheControl = CacheControl.Builder()
                        .onlyIfCached()
                        .maxStale(maxStale, TimeUnit.SECONDS)
                        .build()
                request = request.newBuilder()
                        .cacheControl(tempCacheControl)
                        .build()
                Log.i("GetCache", "intercept:no network ")
            }
            return chain.proceed(request)
        }
    }

    inner class WriteCache : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val originalResponse = chain.proceed(request)
            val maxAge: Int = 1 * 60 // 在线缓存在1分钟内可读取 单位:秒
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build()
        }

    }
}