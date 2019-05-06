package com.yuong.wanandroidkotlin.widget

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.base.MyApplication


/**
 * Created by eado on 2017/2/6.
 */

class EmptyLayout : LinearLayout, View.OnClickListener {// , ISkinUIObserver {

    private var animProgress: ProgressBar? = null
    private var clickEnable = true
    var img: ImageView? = null
    private var listener: View.OnClickListener? = null
    var errorState: Int = 0
        private set
    private var mLayout: LinearLayout? = null
    private var strNoDataContent = ""
    private var tv: TextView? = null

    val isLoadError: Boolean
        get() = errorState == NETWORK_ERROR

    val isLoading: Boolean
        get() = errorState == NETWORK_LOADING

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        val view = View.inflate(context, R.layout.view_error_layout, null)
        img = view.findViewById(R.id.img_error_layout) as ImageView
        tv = view.findViewById(R.id.tv_error_layout) as TextView
        mLayout = view.findViewById(R.id.pageerrLayout) as LinearLayout
        animProgress = view.findViewById(R.id.animProgress) as ProgressBar
        setBackgroundColor(-1)
        setOnClickListener(this)
        img!!.setOnClickListener { v ->
            if (clickEnable) {
                // setErrorType(NETWORK_LOADING);
                if (listener != null) {
                    listener!!.onClick(v)
                }
            }
        }
        addView(view)
        changeErrorLayoutBgMode(context!!)
    }

    fun changeErrorLayoutBgMode(context1: Context) {
        // mLayout.setBackgroundColor(SkinsUtil.getColor(context1,
        // "bgcolor01"));
        // tv.setTextColor(SkinsUtil.getColor(context1, "textcolor05"));
    }

    fun dismiss() {
        errorState = HIDE_LAYOUT
        visibility = View.GONE
    }

    override fun onClick(v: View) {
        if (clickEnable) {
            // setErrorType(NETWORK_LOADING);
            if (listener != null) {
                listener!!.onClick(v)
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // MyApplication.getInstance().getAtSkinObserable().registered(this);
        onSkinChanged()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // MyApplication.getInstance().getAtSkinObserable().unregistered(this);
    }

    fun onSkinChanged() {
        // mLayout.setBackgroundColor(SkinsUtil
        // .getColor(getContext(), "bgcolor01"));
        // tv.setTextColor(SkinsUtil.getColor(getContext(), "textcolor05"));
    }

    fun setDayNight(flag: Boolean) {}

    fun setErrorMessage(msg: String) {
        tv!!.text = msg
    }

    /**
     * 新添设置背景
     *
     * @author 火蚁 2015-1-27 下午2:14:00
     */
    fun setErrorImag(imgResource: Int) {
        try {
            img!!.setImageResource(imgResource)
        } catch (e: Exception) {
        }

    }

    fun setErrorType(i: Int) {
        visibility = View.VISIBLE
        when (i) {
            NETWORK_ERROR -> {
                errorState = NETWORK_ERROR
                // img.setBackgroundDrawable(SkinsUtil.getDrawable(context,"pagefailed_bg"));
                if (hasInternet()) {
                    tv!!.text = "内容加载失败\\r\\n点击重新加载"
                    img!!.setBackgroundResource(R.drawable.pagefailed_bg)
                } else {
                    tv!!.text = "没有可用的网络！"
                    img!!.setBackgroundResource(R.drawable.page_icon_network)
                }
                img!!.visibility = View.VISIBLE
                animProgress!!.visibility = View.GONE
                clickEnable = true
            }
            NETWORK_LOADING -> {
                errorState = NETWORK_LOADING
                // animProgress.setBackgroundDrawable(SkinsUtil.getDrawable(context,"loadingpage_bg"));
                animProgress!!.visibility = View.VISIBLE
                img!!.visibility = View.GONE
                tv!!.text = "加载中…"
                clickEnable = false
            }
            NODATA -> {
                errorState = NODATA
                // img.setBackgroundDrawable(SkinsUtil.getDrawable(context,"page_icon_empty"));
                img!!.setBackgroundResource(R.drawable.page_icon_empty)
                img!!.visibility = View.VISIBLE
                animProgress!!.visibility = View.GONE
                setTvNoDataContent()
                clickEnable = true
            }
            HIDE_LAYOUT -> visibility = View.GONE
            NODATA_ENABLE_CLICK -> {
                errorState = NODATA_ENABLE_CLICK
                img!!.setBackgroundResource(R.drawable.page_icon_empty)
                // img.setBackgroundDrawable(SkinsUtil.getDrawable(context,"page_icon_empty"));
                img!!.visibility = View.VISIBLE
                animProgress!!.visibility = View.GONE
                setTvNoDataContent()
                clickEnable = true
            }
            else -> {
            }
        }
    }

    fun setNoDataContent(noDataContent: String) {
        strNoDataContent = noDataContent
    }

    fun setOnLayoutClickListener(listener: View.OnClickListener) {
        this.listener = listener
    }

    fun setTvNoDataContent() {
        if ("" != strNoDataContent) {
            tv!!.text = strNoDataContent
        } else {
            tv!!.text = "暂无内容！"
        }
    }

    override fun setVisibility(visibility: Int) {
        if (visibility == View.GONE) {
            errorState = HIDE_LAYOUT
        }
        super.setVisibility(visibility)
    }

    companion object {

        val HIDE_LAYOUT = 4
        val NETWORK_ERROR = 1
        val NETWORK_LOADING = 2
        val NODATA = 3
        val NODATA_ENABLE_CLICK = 5
        val NO_LOGIN = 6

        @SuppressLint("MissingPermission")
        fun hasInternet(): Boolean {
            val flag: Boolean
            flag = (MyApplication.context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo != null
            return flag
        }
    }
}

