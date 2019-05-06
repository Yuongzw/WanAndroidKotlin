package com.yuong.wanandroidkotlin.ui.login.model

import android.content.Context
import com.yuong.wanandroidkotlin.base.BaseModel
import com.yuong.wanandroidkotlin.bean.RegisterAndLoginBean
import com.yuong.wanandroidkotlin.net.BaseLoadListener
import com.yuong.wanandroidkotlin.subscriber.PRSubscriber
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoginModelImpl(context: Context): BaseModel(context), LoginModel {


    override fun login(username: String, password: String, loadListener: BaseLoadListener<Any>) {
        api.login(username, password)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
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

    override fun register(username: String, password: String, repassword: String, loadListener: BaseLoadListener<Any>) {
        api.register(username, password, repassword)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
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

    override fun cancelLogin() {
        clearPool()
    }
}