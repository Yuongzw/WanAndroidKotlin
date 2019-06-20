package com.yuong.wanandroidkotlin.util

import android.content.Context

class SizeUtils {
    companion object {

        fun px2dip(context: Context, pxValue:Float):Int {
            val scale = context.getResources().getDisplayMetrics().density
            return (pxValue / scale + 0.5f).toInt()
        }

        fun dip2px(context: Context, dipValue:Float):Int{
            val scale = context.getResources().getDisplayMetrics().density
            return  (dipValue * scale + 0.5f).toInt()
        }

        fun px2sp(context: Context, pxValue:Float):Int{
            val fontScale = context.getResources().getDisplayMetrics().scaledDensity
            return  (pxValue / fontScale + 0.5f).toInt()
        }

        fun sp2px(context: Context, spValue:Float):Int{
            val fontScale = context.getResources().getDisplayMetrics().scaledDensity
            return (spValue * fontScale + 0.5f).toInt()
        }
    }
}