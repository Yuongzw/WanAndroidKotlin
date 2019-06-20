package com.yuong.wanandroidkotlin.skin

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat

import java.io.File
import java.lang.reflect.Method

class SkinEngine private constructor() {

    private var mOutResource: Resources? = null// TODO: 资源管理器
    private var mContext: Context? = null//上下文
    private var mOutPkgName: String? = null// TODO: 外部资源包的packageName

    fun init(context: Context) {
        mContext = context.applicationContext
        //使用application的目的是，如果万一传进来的是Activity对象
        //那么它被静态对象instance所持有，这个Activity就无法释放了
    }

    /**
     * TODO: 加载外部资源包
     */
    fun load(path: String) {//path 是外部传入的apk文件名
        val file = File(path)
        if (!file.exists()) {
            return
        }
        //取得PackageManager引用
        val mPm = mContext!!.packageManager
        //“检索在包归档文件中定义的应用程序包的总体信息”，说人话，外界传入了一个apk的文件路径，这个方法，拿到这个apk的包信息,这个包信息包含什么？
        val mInfo = mPm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES)
        mOutPkgName = mInfo.packageName//先把包名存起来
        val assetManager: AssetManager//资源管理器
        try {
            //TODO: 关键技术点3 通过反射获取AssetManager 用来加载外面的资源包
            assetManager = AssetManager::class.java.newInstance()//反射创建AssetManager对象，为何要反射？使用反射，是因为他这个类内部的addAssetPath方法是hide状态
            //addAssetPath方法可以加载外部的资源包
            val addAssetPath = assetManager.javaClass.getMethod("addAssetPath", String::class.java)//为什么要反射执行这个方法？因为它是hide的,不直接对外开放，只能反射调用
            addAssetPath.invoke(assetManager, path)//反射执行方法
            mOutResource = Resources(assetManager, //参数1，资源管理器
                    mContext!!.resources.displayMetrics, //这个好像是屏幕参数
                    mContext!!.resources.configuration)//资源配置
            //最终创建出一个 "外部资源包"mOutResource ,它的存在，就是要让我们的app有能力加载外部的资源文件
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 提供外部资源包里面的颜色
     * @param resId
     * @return
     */
    fun getColor(resId: Int): Int {
        if (mOutResource == null) {
            return resId
        }
        val resName = mOutResource!!.getResourceEntryName(resId)
        val outResId = mOutResource!!.getIdentifier(resName, "color", mOutPkgName)
        return if (outResId == 0) {
            resId
        } else mOutResource!!.getColor(outResId)
    }

    /**
     * 提供外部资源包里的图片资源
     * @param resId
     * @return
     */
    fun getDrawable(resId: Int): Drawable? {//获取图片
        if (mOutResource == null) {
            return ContextCompat.getDrawable(mContext!!, resId)
        }
        val resName = mOutResource!!.getResourceEntryName(resId)
        val outResId = mOutResource!!.getIdentifier(resName, "drawable", mOutPkgName)
        return if (outResId == 0) {
            ContextCompat.getDrawable(mContext!!, resId)
        } else mOutResource!!.getDrawable(outResId)
    }

    companion object {

        //单例
        @SuppressLint("StaticFieldLeak")
        val instance = SkinEngine()
    }

    //..... 这里还可以提供外部资源包里的String，font等等等，只不过要手动写代码来实现getXX方法
}
