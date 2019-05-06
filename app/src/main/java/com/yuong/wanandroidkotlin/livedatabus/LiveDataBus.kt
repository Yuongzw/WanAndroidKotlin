package com.yuong.wanandroidkotlin.livedatabus

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.os.Handler
import android.os.Looper

import java.util.HashMap
import java.util.Map

class LiveDataBus private constructor() {

    private val bus: MutableMap<String, BusMutableLiveData<Any>>

    init {
        bus = HashMap()
    }

    private object SingletonHolder {
        val DEFAULT_BUS = LiveDataBus()
    }

    @Synchronized
    fun <T> with(key: String, type: Class<T>): BusMutableLiveData<T> {
        if (!bus.containsKey(key)) {
            bus[key] = BusMutableLiveData()
        }
        return bus[key] as BusMutableLiveData<T>
    }

    fun with(key: String): BusMutableLiveData<Any> {
        return with(key, Any::class.java)
    }

    private class ObserverWrapper<T>(private val observer: Observer<T>?) : Observer<T> {

        private val isCallOnObserve: Boolean
            get() {
                val stackTrace = Thread.currentThread().stackTrace
                if (stackTrace != null && stackTrace.size > 0) {
                    for (element in stackTrace) {
                        if ("android.arch.lifecycle.LiveData" == element.className && "observeForever" == element.methodName) {
                            return true
                        }
                    }
                }
                return false
            }

        override fun onChanged(t: T?) {
            if (observer != null) {
                if (isCallOnObserve) {
                    return
                }
                observer.onChanged(t)
            }
        }
    }

    class BusMutableLiveData<T> : MutableLiveData<T>() {

        private val observerMap = HashMap<Observer<*>, Observer<*>>()
        private val mainHandler = Handler(Looper.getMainLooper())


        private inner class PostValueTask(private val newValue: Any) : Runnable {

            override fun run() {
                value = newValue as T
            }
        }

        override fun postValue(value: T) {
            mainHandler.post(PostValueTask(value!!))
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
            super.observe(owner, observer)
            try {
                hook(observer)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun observeSticky(owner: LifecycleOwner, observer: Observer<T>) {
            super.observe(owner, observer)
        }

        override fun observeForever(observer: Observer<T>) {
            if (!observerMap.containsKey(observer)) {
                observerMap[observer] = ObserverWrapper(observer)
            }
            super.observeForever(observerMap[observer] as Observer<T>)
        }

        fun observeStickyForever(observer: Observer<T>) {
            super.observeForever(observer)
        }

        override fun removeObserver(observer: Observer<T>) {
            var realObserver: Observer<*>? = null
            if (observerMap.containsKey(observer)) {
                realObserver = observerMap.remove(observer)
            } else {
                realObserver = observer
            }
            super.removeObserver((realObserver as Observer<T>?)!!)
        }

        @Throws(Exception::class)
        private fun hook(observer: Observer<T>) {
            //get wrapper's version
            val classLiveData = LiveData::class.java
            val fieldObservers = classLiveData.getDeclaredField("mObservers")
            fieldObservers.isAccessible = true
            val objectObservers = fieldObservers.get(this)
            val classObservers = objectObservers.javaClass
            val methodGet = classObservers.getDeclaredMethod("get", Any::class.java)
            methodGet.isAccessible = true
            val objectWrapperEntry = methodGet.invoke(objectObservers, observer)
            var objectWrapper: Any? = null
            if (objectWrapperEntry is Map.Entry<*, *>) {
                objectWrapper = (objectWrapperEntry as Map.Entry<*, *>).value
            }
            if (objectWrapper == null) {
                throw NullPointerException("Wrapper can not be bull!")
            }
            val classObserverWrapper = objectWrapper.javaClass.superclass
            val fieldLastVersion = classObserverWrapper.getDeclaredField("mLastVersion")
            fieldLastVersion.isAccessible = true
            //get livedata's version
            val fieldVersion = classLiveData.getDeclaredField("mVersion")
            fieldVersion.isAccessible = true
            val objectVersion = fieldVersion.get(this)
            //set wrapper's version
            fieldLastVersion.set(objectWrapper, objectVersion)
        }
    }

    companion object {

        fun get(): LiveDataBus {
            return SingletonHolder.DEFAULT_BUS
        }
    }
}

