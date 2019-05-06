package com.yuong.wanandroidkotlin.util

import android.content.Context
import android.view.inputmethod.InputMethodManager

import java.lang.reflect.Field

object FixMemLeak {

    private var field: Field? = null
    private var hasField = true

    fun fixLeak(context: Context) {
        if (!hasField) {
            return
        }
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                ?: return

        val arr = arrayOf("mLastSrvView")
        for (param in arr) {
            try {
                if (field == null) {
                    field = imm.javaClass.getDeclaredField(param)
                }
                if (field == null) {
                    hasField = false
                }
                if (field != null) {
                    field!!.isAccessible = true
                    field!!.set(imm, null)
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }

        }
    }


}
