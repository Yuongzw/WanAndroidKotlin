package com.yuong.wanandroidkotlin.base

import java.lang.reflect.ParameterizedType

/**
 * Created by apanda on 2017/6/13.
 */
/**
 * 类转换初始化
 */
object TUtil {
    fun <T> getT(o: Any, i: Int): T? {
        try {
            return ((o.javaClass
                    .genericSuperclass as ParameterizedType).getActualTypeArguments()[i] as Class<T>)
                    .newInstance()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }

        return null
    }


    fun <T> getT(o: Any): T? {
        try {
            val type = o.javaClass.genericSuperclass as ParameterizedType
            val actualTypeArguments = type.getActualTypeArguments()
            val clazz = actualTypeArguments[1] as Class<T>
            return clazz.newInstance()!!
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }

        return null
    }


    fun forName(className: String): Class<*>? {
        try {
            return Class.forName(className)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

}