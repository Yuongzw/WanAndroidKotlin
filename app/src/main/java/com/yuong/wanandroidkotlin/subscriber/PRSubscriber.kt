package com.yuong.wanandroidkotlin.subscriber


import com.yuong.wanandroidkotlin.exception.ApiExceptionFactory
import com.yuong.wanandroidkotlin.util.ToastUtils
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

open class PRSubscriber<T>
//    private EmptyLayout emptyLayout;
//    private MyProgressDialog dialog;

protected constructor()//        this(null);
    : Observer<T> {

    //    protected PRSubscriber(Object view) {
    //        if (view  instanceof EmptyLayout) {
    //            this.emptyLayout = (EmptyLayout) view;
    //        } else if (view instanceof MyProgressDialog) {
    //            this.dialog = (MyProgressDialog) view;
    //        }
    //    }

    protected var disposable: Disposable? = null

    override fun onSubscribe(d: Disposable) {
        disposable = d
    }

    override fun onNext(t: T) {

    }

    override fun onError(e: Throwable) {
        //        if (emptyLayout != null) {
        //            emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        //        }
        //        if (dialog != null) {
        //            dialog.dismiss();
        //        }
        ToastUtils.showShort(ApiExceptionFactory.getApiException(e).displayMessage)

    }

    override fun onComplete() {

    }
}
