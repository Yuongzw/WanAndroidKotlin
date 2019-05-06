package com.yuong.wanandroidkotlin.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup

class MyViewPagerAdapter(fm: FragmentManager?, fragments: MutableList<Fragment>) : FragmentPagerAdapter(fm) {
    private var fragments: MutableList<Fragment>? = null

    init {
        this.fragments = fragments
    }
    override fun getItem(position: Int): Fragment {
        return fragments!!.get(position)
    }

    override fun getCount(): Int {
        return fragments!!.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        super.destroyItem(container, position, `object`)
    }
}