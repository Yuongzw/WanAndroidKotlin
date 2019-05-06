package com.yuong.wanandroidkotlin.ui.todo.viewModel

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.databinding.ObservableField
import android.text.style.TtsSpan
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.google.gson.internal.LinkedTreeMap
import com.yuong.wanandroidkotlin.base.BaseViewModel
import com.yuong.wanandroidkotlin.bean.WanAndroidBaseResponse
import com.yuong.wanandroidkotlin.databinding.ActivityTodoDetailBinding
import com.yuong.wanandroidkotlin.net.BaseLoadListener
import com.yuong.wanandroidkotlin.ui.todo.model.TodoModel
import com.yuong.wanandroidkotlin.ui.todo.model.TodoModelImpl
import com.yuong.wanandroidkotlin.util.ToastUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TodoDetailViewModel : BaseViewModel<ActivityTodoDetailBinding>(), BaseLoadListener<Any> {

    private lateinit var model: TodoModel
    var title = ObservableField("")
    var content = ObservableField("")
    var date = ObservableField("")
    var isShow = ObservableField(true)
    var enable = ObservableField(true)
    var btnText = ObservableField("修改")
    var id: Int = 0
    var status: Int = 0
    var mode: Int = 0
    override fun initUi() {
        if (mode == 2) {
            isShow.set(false)
            enable.set(false)
        } else if (mode == 1) {
            isShow.set(true)
            enable.set(true)
            btnText.set("修改")
        } else {
            isShow.set(true)
            enable.set(true)
            btnText.set("添加")
        }

        binding!!.btnSave.setOnClickListener {
            if (title.get() == "") {
                ToastUtils.showShort("标题不能为空！")
                return@setOnClickListener
            }
            if (mode == 0) {//添加
                model.addTodo(title.get()!!, content.get()!!, date.get()!!, 0, this)
                activity!!.setResult(RESULT_OK)
            } else if (mode == 1) {//修改
                model.updateTodo(id, title.get()!!, content.get()!!, date.get()!!, status, 0, this)
            }
        }
        binding!!.btnCancel.setOnClickListener { activity!!.finish() }
        binding!!.etDate.setOnClickListener {
            val pvTime = TimePickerBuilder(activity, OnTimeSelectListener { date, v ->
                val t = getDate(date)
                if (isDate2Bigger(t, getDate(Date()))) {
                    this.date.set(t)
                } else {
                    ToastUtils.showShort("选择的日期不能比今天小！")
                }
            }).build()
            pvTime.show()
        }
        initNet()
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDate(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(date)
    }

    /**
     * 比较两个日期的大小，日期格式为yyyy-MM-dd
     *
     * @param date1 the first date
     * @param date2 the second date
     * @return true <br></br>false
     */
    @SuppressLint("SimpleDateFormat")
    private fun isDate2Bigger(date1: String, date2: String): Boolean {
        var isBigger = false
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var dt1: Date? = null
        var dt2: Date? = null
        try {
            dt1 = sdf.parse(date1)
            dt2 = sdf.parse(date2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        if (dt1!!.time >= dt2!!.time) {
            isBigger = true
        } else if (dt1.time < dt2.time) {
            isBigger = false
        }
        return isBigger
    }

    override fun initNet() {
        model = TodoModelImpl(activity!!.applicationContext)
    }

    override fun loadSuccess(o: Any) {
        if (o is WanAndroidBaseResponse) {
            val response = o
            if (response.errorCode == 0) {
                activity!!.setResult(RESULT_OK)
                activity!!.finish()
            } else {
                ToastUtils.showShort(response.errorMsg)
            }
        }
        if (o is LinkedTreeMap<*, *>) {
            activity!!.setResult(RESULT_OK)
            activity!!.finish()
        }
    }

    override fun loadFailure(message: String) {
    }

    override fun loadStart() {
    }

    override fun loadComplete() {
    }

}