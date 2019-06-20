package com.yuong.wanandroidkotlin.ui.main.ui

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.ViewPager
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.daimajia.numberprogressbar.NumberProgressBar
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder
import com.yuong.wanandroidkotlin.MyConstans
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.base.BaseActivity
import com.yuong.wanandroidkotlin.base.MyApplication
import com.yuong.wanandroidkotlin.bean.AppInfoBean
import com.yuong.wanandroidkotlin.databinding.ActivityMainBinding
import com.yuong.wanandroidkotlin.livedatabus.LiveDataBus
import com.yuong.wanandroidkotlin.ui.collect.ui.CollectActivity
import com.yuong.wanandroidkotlin.ui.login.ui.LoginActivity
import com.yuong.wanandroidkotlin.ui.main.viewModel.MainViewModel
import com.yuong.wanandroidkotlin.ui.search.ui.SearchActivity
import com.yuong.wanandroidkotlin.ui.setting.ui.SettingActivity
import com.yuong.wanandroidkotlin.ui.todo.ui.activity.TodoActivity
import com.yuong.wanandroidkotlin.util.*
import com.yuong.wanandroidkotlin.widget.SmoothCheckBox
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.title_bar.*

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    private var headerView: FrameLayout? = null
    private lateinit var tv_username: TextView
    private val titles: Array<String> = arrayOf("首页", "体系", "公众号", "项目")
    private lateinit var dialogView: View
    private lateinit var ll_content: LinearLayout
    private lateinit var tv_update_log: TextView
    private lateinit var tv_update_time: TextView
    private lateinit var update_version: TextView
    private lateinit var current_version: TextView
    private lateinit var ll_progress: LinearLayout
    private lateinit var smoothCheckBox: SmoothCheckBox
    private lateinit var numberProgressBar: NumberProgressBar

    private lateinit var dialogBuilder: NiftyDialogBuilder

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
        initDialogView()
        initDownload()

    }

    private fun initDialogView() {
        dialogBuilder = NiftyDialogBuilder.getInstance(this)
        dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_update, null)
        tv_update_log = dialogView.findViewById(R.id.tv_update_log)
        tv_update_time = dialogView.findViewById(R.id.tv_update_time)
        current_version = dialogView.findViewById(R.id.current_version)
        update_version = dialogView.findViewById(R.id.update_version)
        ll_content = dialogView.findViewById(R.id.ll_content)
        ll_progress = dialogView.findViewById(R.id.ll_progress)
        smoothCheckBox = dialogView.findViewById(R.id.smoothCheckBox)
        numberProgressBar = dialogView.findViewById(R.id.numberProgressBar)
        numberProgressBar.max = 100
    }

    @SuppressLint("SetTextI18n")
    fun showDownloadDialog(appInfo: AppInfoBean) {
        val updateLog = appInfo.data!!.buildUpdateDescription
        val split = updateLog!!.split("；".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        val builder = StringBuilder().append("更新内容:\n")
        for (aSplit in split) {
            builder.append(aSplit)
        }
        builder.deleteCharAt(builder.length - 1)
        tv_update_log.text = builder.toString()
        tv_update_time.setText(appInfo.data!!.buildUpdated)
        current_version.text = "当前版本：${AppUtil.getVersionName(this.applicationContext)}"
        update_version.text = "最新版本：" + appInfo.data!!.buildVersion

        dialogBuilder
                .withTitle("发现新版本")
                .withTitleColor("#FFFFFF")
                .withDividerColor("#11000000")
                .withMessage(null)
                .withMessageColor("#FFCFCFCF")
                .withDialogColor("#AF444D50")
                .withIcon(getResources().getDrawable(R.drawable.logo))
                .withDuration(700)
                .withEffect(Effectstype.SlideBottom)
                .withButton1Text("取消")
                .withButton2Text(getString(R.string.update))
                .isCancelableOnTouchOutside(false)
                .setCustomView(dialogView, this)
                .setButton1Click({
                    dialogBuilder.dismiss()
                    MyDownloadUtils.get().stopDownload()
                })
                .setButton2Click({
                    ll_content.visibility = View.GONE
                    ll_progress.visibility = View.VISIBLE
                    MyDownloadUtils.get().startDownload("https://www.pgyer.com/apiv2/app/install?&_api_key=${MyConstans.API_Key}&appKey=${MyConstans.App_Key}")
                    dialogBuilder.setButton2Click(null)
                })
                .show()

        dialogBuilder.setOnKeyListener { dialogInterface, keyCode, keyEvent -> keyCode == KeyEvent.KEYCODE_BACK }
        dialogBuilder.setOnDismissListener {
            ll_content.visibility = View.VISIBLE
            ll_progress.visibility = View.GONE
            SharedPreferenceUtil.saveData(MyApplication.instance!!,"isPopUp", smoothCheckBox.isChecked)
        }

    }

    fun initDownload() {
        MyDownloadUtils.get().initDownload(MyConstans.DEFAULT_SAVE_FILE_PATH)
        MyDownloadUtils.get().setListener(object : DownloadListener {
            override fun startDownload() {

            }

            override fun stopDownload() {}

            override fun finishDownload() {
                viewModle!!.installApk(MyDownloadUtils.get().downloadFile)
                runOnUiThread {
                    dialogBuilder.dismiss()
                    ll_progress.visibility = View.GONE
                    ll_content.visibility = View.VISIBLE
                    numberProgressBar.progress = 0
                }

            }

            override fun downloadProgress(progress: Long) {
                runOnUiThread { numberProgressBar.progress = progress.toInt() }
            }
        })
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
                } else if (i == R.id.version_up) {
                    viewModle!!.getAppInfo()
                } else if(i == R.id.setting) {
                    startActivity(Intent().setClass(this@MainActivity, SettingActivity::class.java))
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            viewModle!!.installApk(MyDownloadUtils.get()!!.downloadFile)
        }
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
