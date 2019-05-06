package com.yuong.wanandroidkotlin.ui.main.viewModel

import android.support.v4.app.Fragment
import com.yuong.wanandroidkotlin.MyConstans
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.ui.adapter.MyViewPagerAdapter
import com.yuong.wanandroidkotlin.base.BaseViewModel
import com.yuong.wanandroidkotlin.bean.RegisterAndLoginBean
import com.yuong.wanandroidkotlin.bean.WanAndroidBaseResponse
import com.yuong.wanandroidkotlin.databinding.ActivityMainBinding
import com.yuong.wanandroidkotlin.ui.home.ui.HomeFragment
import com.yuong.wanandroidkotlin.net.BaseLoadListener
import com.yuong.wanandroidkotlin.ui.main.model.MainModel
import com.yuong.wanandroidkotlin.ui.main.model.MainModelImpl
import com.yuong.wanandroidkotlin.ui.project.ui.ProjectFragment
import com.yuong.wanandroidkotlin.ui.tree.ui.TreeFragment
import com.yuong.wanandroidkotlin.ui.wechat.ui.WxFragment
import com.yuong.wanandroidkotlin.util.ToastUtils

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
        }
        binding!!.navigationView.getMenu().findItem(R.id.logout).setVisible(MyConstans.ISLOGIN)
    }

    override fun loadFailure(message: String) {
    }

    override fun loadStart() {
    }

    override fun loadComplete() {
    }

}