package com.yuong.wanandroidkotlin.net

import android.content.Context
import android.util.Log
import com.yuong.wanandroidkotlin.MyConstans
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
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

        @Synchronized
        fun get(): MyRetrofit {
            //细心的小伙伴肯定发现了，这里不用getInstance作为为方法名，是因为在伴生对象声明时，内部已有getInstance方法，所以只能取其他名字
            return instance!!
        }
    }

    var api: NetApi? = null


    private val cookies = HashSet<String>()

    private constructor() {
        var client = OkHttpClient.Builder()
                .addInterceptor(AddCookiesInterceptor())
                .addInterceptor(GetCookiesInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS).build()
        var retrofit = Retrofit.Builder()
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())////增加返回值为String的支持
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(MyConstans.BASE_URL)
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
}