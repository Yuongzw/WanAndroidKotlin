package com.yuong.wanandroidkotlin.ui.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
import com.yuong.wanandroidkotlin.bean.WXNumbersBean
import com.yuong.wanandroidkotlin.ui.wechat.ui.WxArticleFragment

class MyWXVPAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {
    private var titles: MutableList<String>
    private lateinit var datas: List<WXNumbersBean.DataBean>

    init {
        titles = ArrayList()
    }

    fun setList(datas: List<WXNumbersBean.DataBean>) {
        this.datas = datas
        this.titles.clear()
        for (dataBean in datas) {
            this.titles.add(dataBean.name!!)
        }
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment {
        val fragment = WxArticleFragment()
        val bundle = Bundle()
        bundle.putInt("id", datas.get(position).id)
        fragment.setArguments(bundle)
        return fragment
    }


    override fun getCount(): Int {
        return titles.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title =  titles.get(position)
        if (title == null) {
            title = ""
        }
        return title
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        super.destroyItem(container, position, `object`)
    }
}