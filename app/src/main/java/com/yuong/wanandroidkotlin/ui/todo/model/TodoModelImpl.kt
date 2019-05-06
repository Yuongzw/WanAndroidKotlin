package com.yuong.wanandroidkotlin.ui.todo.model

import android.content.Context
import com.google.gson.internal.LinkedTreeMap
import com.yuong.wanandroidkotlin.base.BaseModel
import com.yuong.wanandroidkotlin.bean.TodoListBean
import com.yuong.wanandroidkotlin.bean.WanAndroidBaseResponse
import com.yuong.wanandroidkotlin.net.BaseLoadListener
import com.yuong.wanandroidkotlin.subscriber.PRSubscriber
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TodoModelImpl(context: Context): BaseModel(context), TodoModel {
    override fun getTodoList(type: Int, page: Int, loadListener: BaseLoadListener<Any>) {
        api.getTodoList(type, page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : PRSubscriber<TodoListBean>(){
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        addDisposable(d)
                    }

                    override fun onNext(t: TodoListBean) {
                        loadListener.loadSuccess(t)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        deleteDisposable(disposable!!)
                        loadListener.loadFailure(e.message!!)
                    }
                })

    }

    override fun getTodoDoneList(type: Int, page: Int, loadListener: BaseLoadListener<Any>) {
        api.getTodoDoneList(type, page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : PRSubscriber<TodoListBean>(){
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        addDisposable(d)
                    }

                    override fun onNext(t: TodoListBean) {
                        loadListener.loadSuccess(t)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        deleteDisposable(disposable!!)
                        loadListener.loadFailure(e.message!!)
                    }
                })

    }

    override fun addTodo(title: String, content: String, date: String, type: Int, loadListener: BaseLoadListener<Any>) {
        api.addTodo(title, content, date, type)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : PRSubscriber<WanAndroidBaseResponse>(){
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

    override fun updateTodo(id: Int, title: String, content: String, date: String, status: Int, type: Int, loadListener: BaseLoadListener<Any>) {
        api.updateTodo(id,title, content, date, status, type)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : PRSubscriber<WanAndroidBaseResponse>(){
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        addDisposable(d)
                    }

                    override fun onNext(t: WanAndroidBaseResponse) {
                        val treeMap = t.data as LinkedTreeMap<*, *>
                        loadListener.loadSuccess(treeMap)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        deleteDisposable(disposable!!)
                        loadListener.loadFailure(e.message!!)
                    }
                })
    }

    override fun deleteTodo(id: Int, loadListener: BaseLoadListener<Any>) {
        api.deleteTodo(id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : PRSubscriber<WanAndroidBaseResponse>(){
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