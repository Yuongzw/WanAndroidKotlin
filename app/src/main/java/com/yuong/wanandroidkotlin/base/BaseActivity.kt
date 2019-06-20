package com.yuong.wanandroidkotlin.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.yuong.wanandroidkotlin.skin.SkinEngine
import com.yuong.wanandroidkotlin.skin.SkinFactory
import com.yuong.wanandroidkotlin.util.FixMemLeak
import kotlinx.android.synthetic.main.title_bar.*
import java.io.File

abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel<V>> : AppCompatActivity() {
    protected var binding: V? = null
    protected var viewModle: VM? = null

    protected var skins = arrayOf("skin.apk", "skin2.apk")
    protected var mCurrentSkin: String? = null

    private var mSkinFactory: SkinFactory? = null
    override fun onCreate(savedInstanceState: Bundle?) {

//        mSkinFactory = SkinFactory()
//        mSkinFactory!!.setDelegate(delegate)
//        val layoutInflater = LayoutInflater.from(this)
//        Log.d("layoutInflaterTag", layoutInflater.toString())
//        layoutInflater.factory2 = mSkinFactory
        //状态栏透明化
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT
            //            getWindow().setNavigationBarColor(Color.BLACK);
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutResource())
        viewModle = TUtil.getT(this, 1)!!
        viewModle!!.init(this, binding!!, this.baseContext)
        initData()
    }

    abstract fun getLayoutResource(): Int

    abstract fun initData()

    private fun tag(): String? {
        return this.localClassName
    }

    override fun onResume() {
        super.onResume()
        if (null != mCurrentSkin)
            changeSkin(mCurrentSkin!!) // 换肤操作必须在setContentView之后
    }

    /**
     * 做一个切换方法
     *
     * @return
     */
    fun getPath(): String {
        val path: String
        if (null == mCurrentSkin) {
            path = skins[0]
        } else if (skins[0] == mCurrentSkin) {
            path = skins[1]
        } else if (skins[1] == mCurrentSkin) {
            path = skins[0]
        } else {
            return "unknown skin"
        }
        return path
    }

    fun changeSkin(path: String) {
//        val skinFile = File(cacheDir, path)
        val skinFile = File(Environment.getExternalStorageDirectory(), path)
        SkinEngine.instance.load(skinFile.getAbsolutePath())
        mSkinFactory!!.changeSkin()
        mCurrentSkin = path
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModle!!.destroy()
        FixMemLeak.fixLeak(this)
    }
}