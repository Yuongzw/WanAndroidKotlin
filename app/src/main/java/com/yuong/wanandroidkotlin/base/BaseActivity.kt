package com.yuong.wanandroidkotlin.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.yuong.wanandroidkotlin.util.FixMemLeak
import kotlinx.android.synthetic.main.title_bar.*

abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel<V>> : AppCompatActivity() {
    protected var binding: V? = null
    protected var viewModle: VM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
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

    override fun onDestroy() {
        super.onDestroy()
        viewModle!!.destroy()
        FixMemLeak.fixLeak(this)
    }
}