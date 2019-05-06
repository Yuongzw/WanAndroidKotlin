package com.yuong.wanandroidkotlin.ui.main.model

import android.content.Context
import com.yuong.wanandroidkotlin.base.BaseModel
import com.yuong.wanandroidkotlin.bean.HomeBean
import com.yuong.wanandroidkotlin.bean.TreeArticleBean
import com.yuong.wanandroidkotlin.bean.TreeBean
import com.yuong.wanandroidkotlin.bean.WanAndroidBaseResponse
import com.yuong.wanandroidkotlin.net.BaseLoadListener
import com.yuong.wanandroidkotlin.subscriber.PRSubscriber
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TreeModelImpl(context: Context) : BaseModel(context), TreeModel {

    override fun loadTreeData(loadListener: BaseLoadListener<Any>) {
        api.getTreeData()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : PRSubscriber<TreeBean>() {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        addDisposable(d)
                    }

                    override fun onNext(t: TreeBean) {
                        loadListener.loadSuccess(t)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        deleteDisposable(disposable!!)
                        loadListener.loadFailure(e.message!!)
                    }
                })
    }

    override fun loadTreeArticleData(page: Int, cid: Int, loadListener: BaseLoadListener<Any>) {
        api.getTreeArticleData(page, cid)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : PRSubscriber<HomeBean>() {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        addDisposable(d)
                    }

                    override fun onNext(t: HomeBean) {
                        if (t.data!!.datas != null && t.data!!.datas!!.size > 0) {
                            loadListener.loadSuccess(t)
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