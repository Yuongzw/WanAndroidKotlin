package com.yuong.wanandroidkotlin.ui.search.model

import android.content.Context
import com.yuong.wanandroidkotlin.base.BaseModel
import com.yuong.wanandroidkotlin.bean.HomeBean
import com.yuong.wanandroidkotlin.bean.HotKeyBean
import com.yuong.wanandroidkotlin.bean.SearchResultBean
import com.yuong.wanandroidkotlin.bean.WanAndroidBaseResponse
import com.yuong.wanandroidkotlin.net.BaseLoadListener
import com.yuong.wanandroidkotlin.subscriber.PRSubscriber
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SearchModelImpl(context: Context): BaseModel(context), SearchModel {

    override fun loadHotKeyData(loadListener: BaseLoadListener<Any>) {
        api.getHotKeyData()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : PRSubscriber<HotKeyBean>() {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        addDisposable(d)
                    }

                    override fun onNext(t: HotKeyBean) {
                        loadListener.loadSuccess(t)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        deleteDisposable(disposable!!)
                        loadListener.loadFailure(e.message!!)
                    }
                })
    }

    override fun loadSearchData(page: Int, keyWord: String, loadListener: BaseLoadListener<Any>) {
        api.getSearchData(page, keyWord)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : PRSubscriber<SearchResultBean>() {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        addDisposable(d)
                    }

                    override fun onNext(t: SearchResultBean) {
                        val datas = t.data!!.datas
                        if (datas != null && datas.size > 0) {
                            for (j in datas.indices) {
                                val split = datas.get(j).title!!.split("<em class='highlight'>") //将字符串按 <em class='highlight'> 分为两半，得到一个新的数组
                                var str = ""
                                var str1 = ""
                                for (i in split.indices) {
                                    str += split[i]
                                }
                                val split1 = str.split("</em>".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                for (i in split1.indices) {
                                    str1 += split1[i]
                                }
                                datas.get(j).title = str1
                            }
                            loadListener.loadSuccess(datas)
                        } else {
                            loadListener.loadSuccess(0)
                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        deleteDisposable(disposable!!)
                        loadListener.loadFailure(e.message!!)
                    }
                })
    }

    override fun collectArticle(id: Int, loadListener: BaseLoadListener<Any>) {
        api.collectArticle(id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : PRSubscriber<WanAndroidBaseResponse>() {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        addDisposable(d)
                    }

                    override fun onNext(t: WanAndroidBaseResponse) {
                        loadListener.loadSuccess(t)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        deleteDisposable(disposable!!)
                        loadListener.loadFailure(e.message!!)
                    }
                })
    }

    override fun cancelCollected(id: Int, loadListener: BaseLoadListener<Any>) {
        api.cancelColleted(id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : PRSubscriber<WanAndroidBaseResponse>() {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        addDisposable(d)
                    }

                    override fun onNext(t: WanAndroidBaseResponse) {
                        loadListener.loadSuccess(t)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        deleteDisposable(disposable!!)
                        loadListener.loadFailure(e.message!!)
                    }
                })
    }
}