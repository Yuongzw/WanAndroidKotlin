package com.yuong.wanandroidkotlin.ui.main.viewModel

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.annotation.IntegerRes
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import com.yuong.wanandroidkotlin.MyConstans
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.base.BaseActivity
import com.yuong.wanandroidkotlin.ui.adapter.MyViewPagerAdapter
import com.yuong.wanandroidkotlin.base.BaseViewModel
import com.yuong.wanandroidkotlin.bean.AppInfoBean
import com.yuong.wanandroidkotlin.bean.RegisterAndLoginBean
import com.yuong.wanandroidkotlin.bean.WanAndroidBaseResponse
import com.yuong.wanandroidkotlin.databinding.ActivityMainBinding
import com.yuong.wanandroidkotlin.ui.home.ui.HomeFragment
import com.yuong.wanandroidkotlin.net.BaseLoadListener
import com.yuong.wanandroidkotlin.ui.main.model.MainModel
import com.yuong.wanandroidkotlin.ui.main.model.MainModelImpl
import com.yuong.wanandroidkotlin.ui.main.ui.MainActivity
import com.yuong.wanandroidkotlin.ui.project.ui.ProjectFragment
import com.yuong.wanandroidkotlin.ui.tree.ui.TreeFragment
import com.yuong.wanandroidkotlin.ui.wechat.ui.WxFragment
import com.yuong.wanandroidkotlin.util.AppUtil
import com.yuong.wanandroidkotlin.util.ToastUtils
import java.io.File

class MainViewModel : BaseViewModel<ActivityMainBinding>(), BaseLoadListener<Any> {

    lateinit var adapter: MyViewPagerAdapter
    lateinit var mFragments: MutableList<Fragment>
    private lateinit var model: MainModel

    override fun initUi() {
        binding!!.navigationView.itemIconTintList = null
        initFragments()
        initNet()
    }

    fun initFragments() {
        val homeFragment = HomeFragment()
        val treeFragment = TreeFragment()
        val wxFragment = WxFragment()
        val projectFragment = ProjectFragment()
        mFragments = ArrayList()
        mFragments.add(homeFragment)
        mFragments.add(treeFragment)
        mFragments.add(wxFragment)
        mFragments.add(projectFragment)
        adapter = MyViewPagerAdapter(activity!!.supportFragmentManager, mFragments)
        binding!!.mainViewPager.adapter = adapter

    }

    override fun initNet() {
        model = MainModelImpl(activity!!.applicationContext)
        model.login("234567", "234567", this)
    }

    fun logout() {
        model.logout(this)
    }

    fun getAppInfo() {
        model.getAppInfo(MyConstans.API_Key, MyConstans.App_Key, this)
    }

    override fun loadSuccess(t: Any) {
        if (t is RegisterAndLoginBean) {
            val registerAndLoginBean = t
            if (registerAndLoginBean.errorCode == -1) {
                ToastUtils.showShort(registerAndLoginBean.errorMsg)
                MyConstans.ISLOGIN = false
            } else {    //注册成功
                MyConstans.ISLOGIN = true
                val collectIds = registerAndLoginBean.data!!.collectIds
                if (collectIds != null && collectIds!!.size > 0) {
                    MyConstans.collectIds.clear()
                    for (i in collectIds!!.indices) {
                        val collectId = collectIds!!.get(i) as Double
                        val id = collectId.toInt()
                        MyConstans.collectIds.add(id)
                    }
                }
            }
        } else if (t is WanAndroidBaseResponse) {
            val response = t
            if (response.errorCode == 0) {  //退出登录
                MyConstans.ISLOGIN = false//将登录标记设置为 false
            }
        } else if (t is AppInfoBean) {
            if (t.data!!.buildVersionNo!!.toInt() > AppUtil.getVersionCode(activity!!.applicationContext)) {
                (activity!! as MainActivity).showDownloadDialog(t)
            } else {
                ToastUtils.showShort("当前已是最新版本，无需更新！")
            }
        }
        binding!!.navigationView.getMenu().findItem(R.id.logout).setVisible(MyConstans.ISLOGIN)
    }

    override fun loadFailure(message: String) {
    }

    override fun loadStart() {
    }

    override fun loadComplete() {
    }

    fun installApk(apkfile: File?) {
        ToastUtils.showShort("存放apk路径==" + apkfile!!.getAbsolutePath())
        if (!apkfile.exists()) {
            return
        }
        val intent = Intent(Intent.ACTION_VIEW)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            //                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//无Activity环境启动时添加；
            val apkUri = FileProvider.getUriForFile(activity!!, "com.yuong.wanandroidkotlin.fileprovider", apkfile)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val hasInstallPermission = activity!!.getPackageManager().canRequestPackageInstalls()
            if (hasInstallPermission) {
                //安装应用
                val apkUri = FileProvider.getUriForFile(activity!!, "com.yuong.wanandroidkotlin.fileprovider", apkfile)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
            } else {
                //跳转至“安装未知应用”权限界面，引导用户开启权限
                val selfPackageUri = Uri.parse("package:" + activity!!.getPackageName())
                val intent1 = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, selfPackageUri)
                activity!!.startActivityForResult(intent1, 100)
            }
        } else {
            intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive")

        }
        activity!!.startActivity(intent)
    }

}