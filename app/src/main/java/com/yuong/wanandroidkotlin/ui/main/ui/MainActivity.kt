package com.yuong.wanandroidkotlin.ui.main.ui

import android.animation.ValueAnimator
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.yuong.wanandroidkotlin.MyConstans
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.base.BaseActivity
import com.yuong.wanandroidkotlin.databinding.ActivityMainBinding
import com.yuong.wanandroidkotlin.livedatabus.LiveDataBus
import com.yuong.wanandroidkotlin.ui.collect.ui.CollectActivity
import com.yuong.wanandroidkotlin.ui.login.ui.LoginActivity
import com.yuong.wanandroidkotlin.ui.main.viewModel.MainViewModel
import com.yuong.wanandroidkotlin.ui.search.ui.SearchActivity
import com.yuong.wanandroidkotlin.ui.todo.ui.activity.TodoActivity
import com.yuong.wanandroidkotlin.util.SharedPreferenceUtil
import com.yuong.wanandroidkotlin.util.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation_header.*
import kotlinx.android.synthetic.main.title_bar.*

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    private var headerView: FrameLayout? = null
    private lateinit var tv_username: TextView
    private val titles: Array<String> = arrayOf("首页", "体系", "公众号", "项目")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeMsg()
    }

    override fun initData() {
        iv_back.setVisibility(View.GONE)
        iv_search.setOnClickListener { view ->
            startActivity(Intent(this, SearchActivity::class.java))
        }
        headerView = navigationView.getHeaderView(0) as FrameLayout
        tv_username = headerView!!.findViewById(R.id.tv_username)
        navigationView.getMenu().findItem(R.id.logout).setVisible(false)
        navigationView.setItemIconTintList(null)
        initListener()
        viewModle!!.initUi()

    }

    private fun initListener() {
        navigationView.setNavigationItemSelectedListener(object : NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                val i = item.itemId
                if (i == R.id.my_collected) {
                    if (MyConstans.ISLOGIN) {
                        startActivity(Intent().setClass(this@MainActivity, CollectActivity::class.java))
                    } else {
                        startActivity(Intent().setClass(this@MainActivity, LoginActivity::class.java))
                    }

                } else if (i == R.id.my_todo) {
                    if (MyConstans.ISLOGIN) {
                        startActivity(Intent().setClass(this@MainActivity, TodoActivity::class.java))
                    } else {
                        startActivity(Intent().setClass(this@MainActivity, LoginActivity::class.java))
                    }
                } else if (i == R.id.logout) {
                    viewModle!!.logout()
                }
                drawerLayout.closeDrawers()
                return false
            }
        }
        )
        bottomBar.setOnTabSelectListener { tabId ->
            if (tabId == R.id.tab_home) {
                main_ViewPager.currentItem = 0
            } else if (tabId == R.id.tab_tree) {
                main_ViewPager.currentItem = 1
            } else if (tabId == R.id.tab_wechat) {
                main_ViewPager.currentItem = 2
            } else if (tabId == R.id.tab_project) {
                main_ViewPager.currentItem = 3
            }
        }
        main_ViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {

            }

            override fun onPageSelected(position: Int) {
                tv_title.setText(titles[position])
                bottomBar.selectTabAtPosition(position)
            }

            override fun onPageScrollStateChanged(i: Int) {

            }
        })
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_main
    }

    override fun onResume() {
        super.onResume()
        navigationView.getMenu().findItem(R.id.logout).setVisible(MyConstans.ISLOGIN)
        tv_username.text = SharedPreferenceUtil.getData(applicationContext, "username", "") as String?
    }

    //显示底部导航栏
    private fun showBottomNav(mTarget: View) {
        val va = ValueAnimator.ofFloat(mTarget.y, mTarget.top.toFloat())
        va.duration = 800
        va.addUpdateListener { valueAnimator ->
            mTarget.y = valueAnimator.animatedValue as Float
        }
        va.start()
    }

    private fun hideBottomNav(mTarget: View) {
        val va = ValueAnimator.ofFloat(mTarget.y, mTarget.bottom.toFloat())
        va.duration = 800
        va.addUpdateListener { valueAnimator ->
            mTarget.y = valueAnimator.animatedValue as Float
        }
        va.start()
    }

    fun subscribeMsg() {
        LiveDataBus
                .get()
                .with("bottom", Boolean::class.java)
                .observe(this, Observer<Boolean> { t ->
                    if (t!!) {
                        showBottomNav(bottomBar)
                    } else {
                        hideBottomNav(bottomBar)
                    }
                })
    }

    private var exitTime: Long = 0

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event!!.getAction() == KeyEvent.ACTION_DOWN) run {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.showShort("再按一次退出程序")
                exitTime = System.currentTimeMillis()
            } else run {
                finish()
                System.exit(0)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


}
