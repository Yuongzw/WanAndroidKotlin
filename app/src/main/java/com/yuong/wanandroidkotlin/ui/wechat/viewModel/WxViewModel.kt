package com.yuong.wanandroidkotlin.ui.wechat.viewModel

import android.view.View
import com.yuong.wanandroidkotlin.base.BaseViewModel
import com.yuong.wanandroidkotlin.bean.WXNumbersBean
import com.yuong.wanandroidkotlin.databinding.FragmentWxBinding
import com.yuong.wanandroidkotlin.net.BaseLoadListener
import com.yuong.wanandroidkotlin.ui.adapter.MyWXVPAdapter
import com.yuong.wanandroidkotlin.ui.main.model.WxModel
import com.yuong.wanandroidkotlin.ui.main.model.WxModelImpl
import com.yuong.wanandroidkotlin.widget.EmptyLayout

class WxViewModel:BaseViewModel<FragmentWxBinding>(), BaseLoadListener<Any> {


    private lateinit var model: WxModel
    private lateinit var adapter: MyWXVPAdapter

    override fun initUi() {
        adapter = MyWXVPAdapter(activity!!.supportFragmentManager)
        binding!!.vpWx.adapter = adapter
        binding!!.wxTabLayout.setupWithViewPager(binding!!.vpWx)
        binding!!.wxEmptyLayout.setOnLayoutClickListener(View.OnClickListener {
            binding!!.wxEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING)
            model.loadWXNumbersData(this)
        })
    }

    override fun initNet() {
        model = WxModelImpl(activity!!.applicationContext)
        model.loadWXNumbersData(this)
    }

    override fun loadSuccess(t: Any) {
        if (t is WXNumbersBean) run {
            val wxNumbersBean = t
            if (wxNumbersBean.data != null && wxNumbersBean.data!!.size > 0) {
                binding!!.wxEmptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT)
                binding!!.wxContent.visibility = View.VISIBLE
                adapter.setList(wxNumbersBean.data!!)
            }
        }
    }

    override fun loadFailure(message: String) {
        binding!!.wxEmptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR)
    }

    override fun loadStart() {
    }

    override fun loadComplete() {
    }
}