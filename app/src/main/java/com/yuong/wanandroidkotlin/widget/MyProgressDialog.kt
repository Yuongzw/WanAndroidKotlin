package com.yuong.wanandroidkotlin.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import com.yuong.wanandroidkotlin.R

class MyProgressDialog : Dialog {
    private var textView: TextView? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, theme: Int) : super(context, theme) {
        init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun init() {
        setCancelable(true)
        setCanceledOnTouchOutside(false)
        setContentView(R.layout.loading)//loading的xml文件
        val params = window!!.attributes
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = params
        textView = findViewById(R.id.tv_load_dialog)
    }

    override fun show() {//开启
        super.show()
    }

    override fun dismiss() {//关闭
        super.dismiss()
    }

    fun setText(text: String) {
        if (textView != null) {
            textView!!.text = text
        }
    }


}
