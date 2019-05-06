package com.yuong.wanandroidkotlin.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.ui.main.ui.MainActivity
import com.yuong.wanandroidkotlin.util.BitmapUtil
import com.yuong.wanandroidkotlin.util.FixMemLeak
import kotlinx.android.synthetic.main.activity_splash.*
import yanzhikai.textpath.SyncTextPathView
import yanzhikai.textpath.painter.FireworksPainter

class SplashActivity: AppCompatActivity() {
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
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        atpv_as!!.setPathPainter(FireworksPainter())
        atpv_as!!.startAnimation(0f, 1f)
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000)
    }

    override fun onDestroy() {
        super.onDestroy()
        BitmapUtil.recycleBackgroundBitMap(ll_splash)
        FixMemLeak.fixLeak(this)
    }
}