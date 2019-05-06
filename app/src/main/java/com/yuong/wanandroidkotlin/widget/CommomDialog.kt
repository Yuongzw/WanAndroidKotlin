package com.yuong.wanandroidkotlin.widget

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.yuong.wanandroidkotlin.R

class CommomDialog : Dialog, View.OnClickListener {
    private var titleTxt: TextView? = null
    private var submitTxt: TextView? = null
    private var cancelTxt: TextView? = null
    private var rg_mode: RadioGroup? = null
    private var rb_night: RadioButton? = null
    private var rb_day: RadioButton? = null

    private var mContext: Context? = null
    private var listener: OnCloseListener? = null
    private var positiveName: String? = null
    private var negativeName: String? = null
    private var title: String? = null

    constructor(context: Context) : super(context) {
        this.mContext = context
    }

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {
        this.mContext = context
    }

    constructor(context: Context, themeResId: Int, listener: OnCloseListener) : super(context, themeResId) {
        this.mContext = context
        this.listener = listener
    }

    protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener) : super(context, cancelable, cancelListener) {
        this.mContext = context
    }

    fun setTitle(title: String): CommomDialog {
        this.title = title
        return this
    }

    fun setPositiveButton(name: String): CommomDialog {
        this.positiveName = name
        return this
    }

    fun setNegativeButton(name: String): CommomDialog {
        this.negativeName = name
        return this
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_commom)
        setCanceledOnTouchOutside(false)
        initView()
    }

    private fun initView() {
        titleTxt = findViewById(R.id.title) as TextView
        submitTxt = findViewById(R.id.submit) as TextView
        submitTxt!!.setOnClickListener(this)
        cancelTxt = findViewById(R.id.cancel) as TextView
        cancelTxt!!.setOnClickListener(this)
        rg_mode = findViewById(R.id.rg_mode)
        rb_day = findViewById(R.id.rb_day)
        rb_night = findViewById(R.id.rb_night)

        if (!TextUtils.isEmpty(positiveName)) {
            submitTxt!!.text = positiveName
        }

        if (!TextUtils.isEmpty(negativeName)) {
            cancelTxt!!.text = negativeName
        }

        if (!TextUtils.isEmpty(title)) {
            titleTxt!!.text = title
        }

    }

    override fun onClick(v: View) {
        val i = v.id
        if (i == R.id.cancel) {
            listener?.onClick(this, false)
            this.dismiss()

        } else if (i == R.id.submit) {
            listener?.onClick(this, true)
        }
    }

    interface OnCloseListener {
        fun onClick(dialog: Dialog, confirm: Boolean)
    }
}
