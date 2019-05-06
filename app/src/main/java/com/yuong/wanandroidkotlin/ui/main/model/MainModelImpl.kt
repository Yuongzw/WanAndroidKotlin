package com.yuong.wanandroidkotlin.ui.main.model

import android.content.Context
import com.yuong.wanandroidkotlin.base.BaseModel
import com.yuong.wanandroidkotlin.bean.RegisterAndLoginBean
import com.yuong.wanandroidkotlin.bean.WanAndroidBaseResponse
import com.yuong.wanandroidkotlin.net.BaseLoadListener
import com.yuong.wanandroidkotlin.subscriber.PRSubscriber
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainModelImpl(context: Context) : BaseModel(context), MainModel {


    override fun login(username: String, password: String, loadListener: BaseLoadListener<Any>) {
        api.login(username, password)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : PRSubscriber<RegisterAndLoginBean>() {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        addDisposable(d)
                    }

                    override fun onNext(t: RegisterAndLoginBean) {
                        loadListener.loadSuccess(t)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        deleteDisposable(disposable!!)
                        loadListener.loadFailure(e.message!!)
                    }
                })

    }

    override fun logout(loadListener: BaseLoadListener<Any>) {
        api.logout()
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