package com.yuong.wanandroidkotlin.util

import android.content.Context

object SharedPreferenceUtil {

    //存储的sharedpreferences文件名
    private val FILE_NAME = "wan_android"

    /**
     * 保存数据到文件
     *
     * @param context
     * @param key
     * @param data
     */
    fun saveData(context: Context, key: String, data: Any) {

        val type = data.javaClass.simpleName
        val sharedPreferences = context
                .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        if ("Integer" == type) {
            editor.putInt(key, data as Int)
        } else if ("Boolean" == type) {
            editor.putBoolean(key, data as Boolean)
        } else if ("String" == type) {
            editor.putString(key, data as String)
        } else if ("Float" == type) {
            editor.putFloat(key, data as Float)
        } else if ("Long" == type) {
            editor.putLong(key, data as Long)
        }

        editor.apply()
    }

    /**
     * 从文件中读取数据
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    fun getData(context: Context, key: String, defValue: Any): Any? {

        val type = defValue.javaClass.simpleName
        val sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

        //defValue为为默认值，如果当前获取不到数据就返回它
        if ("Integer" == type) {
            return sharedPreferences.getInt(key, defValue as Int)
        } else if ("Boolean" == type) {
            return sharedPreferences.getBoolean(key, defValue as Boolean)
        } else if ("String" == type) {
            return sharedPreferences.getString(key, defValue as String)
        } else if ("Float" == type) {
            return sharedPreferences.getFloat(key, defValue as Float)
        } else if ("Long" == type) {
            return sharedPreferences.getLong(key, defValue as Long)
        }

        return null
    }

}
