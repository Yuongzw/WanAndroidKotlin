package com.yuong.wanandroidkotlin.ui.search.viewModel

import android.content.Context
import android.content.Intent
import android.databinding.ObservableField
import android.hardware.input.InputManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.base.BaseViewModel
import com.yuong.wanandroidkotlin.bean.HotKeyBean
import com.yuong.wanandroidkotlin.databinding.ActivitySearchBinding
import com.yuong.wanandroidkotlin.net.BaseLoadListener
import com.yuong.wanandroidkotlin.ui.search.model.SearchModel
import com.yuong.wanandroidkotlin.ui.search.model.SearchModelImpl
import com.yuong.wanandroidkotlin.ui.search.ui.SearchResultActivity
import com.yuong.wanandroidkotlin.util.ToastUtils

class SearchViewModel : BaseViewModel<ActivitySearchBinding>(), BaseLoadListener<Any> {

    private lateinit var model: SearchModel
    //关键字
    var searchKey = ObservableField("")
    private var keyWord: String = ""

    override fun initUi() {
        binding!!.tvSearch.setOnClickListener { view ->
            hideKeyboard()
            keyWord = searchKey.get()!!
            if (keyWord.length <= 0) {
                ToastUtils.showShort("输入不能为空")
                return@setOnClickListener
            }
            val intent = Intent(activity, SearchResultActivity::class.java)
            intent.putExtra("keyWord", keyWord)
            activity!!.startActivity(intent)
        }
        initNet()
    }

    fun setFlowLayout(dataBeans: List<HotKeyBean.DataBean>) {
        for (dataBean in dataBeans) {
            val textView = TextView(activity!!.baseContext)   //动态创建 TextView
            textView.setPadding(16, 8, 16, 8) //设置 Padding 值
            textView.setText(dataBean.name)   //设置文字
            textView.textSize = 14f   //设置字体大小
            textView.setBackgroundResource(R.drawable.flow_layout_bg)  //设置背景
            textView.setOnClickListener {
                val intent = Intent(activity, SearchResultActivity::class.java)
                intent.putExtra("keyWord", textView.text)
                activity!!.startActivity(intent)
                ToastUtils.showShort(textView.text as String?)
            }
            binding!!.flowLayout.addView(textView)//添加一个 TextView
        }
    }

    override fun initNet() {
        model = SearchModelImpl(activity!!.applicationContext)
        model.loadHotKeyData(this)
    }

    fun hideKeyboard() {
        val manager = binding!!.searchEditText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(binding!!.searchEditText.windowToken, 0)
    }

    override fun loadSuccess(t: Any) {
        if (t is HotKeyBean) run {
            val hotKeyBean = t
            if (hotKeyBean.data != null && hotKeyBean.data!!.size > 0) {
                setFlowLayout(hotKeyBean.data!!)
            }
        }
    }

    override fun loadFailure(message: String) {
    }

    override fun loadStart() {
    }

    override fun loadComplete() {
    }
}