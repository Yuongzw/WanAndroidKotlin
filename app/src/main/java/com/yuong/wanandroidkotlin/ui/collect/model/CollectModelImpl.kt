package com.yuong.wanandroidkotlin.ui.collect.model

import android.content.Context
import com.yuong.wanandroidkotlin.base.BaseModel
import com.yuong.wanandroidkotlin.bean.CollectArticleBean
import com.yuong.wanandroidkotlin.bean.HomeBean
import com.yuong.wanandroidkotlin.bean.WanAndroidBaseResponse
import com.yuong.wanandroidkotlin.net.BaseLoadListener
import com.yuong.wanandroidkotlin.subscriber.PRSubscriber
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CollectModelImpl(context: Context): BaseModel(context), CollectModel {

    override fun getCollectArticleData(page: Int, loadListener: BaseLoadListener<Any>) {
        api.getCollected(page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : PRSubscriber<HomeBean>() {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        addDisposable(d)
                    }

                    override fun onNext(t: HomeBean) {
                        loadListener.loadSuccess(t)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        deleteDisposable(disposable!!)
                        loadListener.loadFailure(e.message!!)
                    }
                })
    }

    override fun cancelCollectArticle(id: Int, originId: Int, loadListener: BaseLoadListener<Any>) {
        api.cancelColleted(id, originId)
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