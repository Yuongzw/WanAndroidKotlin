package com.yuong.wanandroidkotlin.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

class AppUtil {
    companion object {
        private fun GetVersion(context: Context): PackageInfo? {
            val manager=  context.getPackageManager()
            var info: PackageInfo
            try {
                info = manager.getPackageInfo(context.getPackageName(), 0)
                return info
            }catch (e: PackageManager.NameNotFoundException){
                return null
            }
        }

        fun getVersionName(context: Context):String{
            return try {
                GetVersion(context)!!.versionName
            } catch (e: Exception) {
                "未知"
            }

        }
        fun getVersionCode(context: Context):Int {
            return try {
                GetVersion(context)!!.versionCode
            } catch (e: Exception) {
                0
            }

        }
    }
}