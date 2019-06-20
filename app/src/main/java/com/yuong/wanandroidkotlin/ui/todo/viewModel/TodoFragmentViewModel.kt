package com.yuong.wanandroidkotlin.ui.todo.viewModel

import android.content.DialogInterface
import android.content.Intent
import android.databinding.ObservableField
import android.support.v7.app.AlertDialog
import android.view.View
import com.google.gson.internal.LinkedTreeMap
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.base.BaseViewModel
import com.yuong.wanandroidkotlin.bean.TodoListBean
import com.yuong.wanandroidkotlin.databinding.FragmentTodoBinding
import com.yuong.wanandroidkotlin.livedatabus.LiveDataBus
import com.yuong.wanandroidkotlin.net.BaseLoadListener
import com.yuong.wanandroidkotlin.ui.adapter.MyTodoAdapter
import com.yuong.wanandroidkotlin.ui.todo.model.TodoModel
import com.yuong.wanandroidkotlin.ui.todo.model.TodoModelImpl
import com.yuong.wanandroidkotlin.ui.todo.ui.activity.TodoDetailActivity

class TodoFragmentViewModel : BaseViewModel<FragmentTodoBinding>(), BaseLoadListener<Any>, MyTodoAdapter.OnTodoClickListener {

    private lateinit var model: TodoModel
    var status: Int = 0
    private var page: Int = 1
    private var groupTodoList: MutableList<String> = ArrayList()
    private var childTodoList: MutableList<MutableList<TodoListBean.DataBean.DatasBean>> = ArrayList()
    var isShow = ObservableField(true)
    private lateinit var adapter: MyTodoAdapter
    private var groupPosition: Int = 0
    private var childPosition:Int = 0

    override fun initUi() {
        adapter = MyTodoAdapter(groupTodoList, childTodoList)
        adapter.onTodoClickListener = this
        binding!!.todoList.setAdapter(adapter)
        if (status == 0) {
            binding!!.ivAdd.visibility = View.VISIBLE
            binding!!.tvTodoTitle.text = "代办清单"
            binding!!.todoConstraintLayout.setBackgroundColor(activity!!.resources.getColor(R.color.green))
        } else{
            binding!!.ivAdd.visibility = View.GONE
            binding!!.tvTodoTitle.text = "已完成清单"
            binding!!.todoConstraintLayout.setBackgroundColor(activity!!.resources.getColor(R.color.orange))
        }
        if (status == 0) {
            isShow.set(true)
        } else {
            isShow.set(false)
        }
        binding!!.ivAdd.setOnClickListener {
            val intent = Intent(activity, TodoDetailActivity::class.java)  //弹出对话框
            intent.putExtra("mode", 0)//0：添加；1：修改；2：查看
            activity!!.startActivityForResult(intent, 0)
        }
    }

    override fun initNet() {
        model = TodoModelImpl(activity!!.applicationContext)
        if (status == 0) {
            model.getTodoList(0, page, this)
        } else{
            model.getTodoDoneList(0, page, this)
        }
    }

    fun addTodo(datasBean: TodoListBean.DataBean.DatasBean) {
        if (groupTodoList.contains(datasBean.dateStr)) {
            val index = groupTodoList.indexOf(datasBean.dateStr)
            childTodoList[index].add(datasBean)
        } else {
            val todo = ArrayList<TodoListBean.DataBean.DatasBean>()
            todo.add(datasBean)
            groupTodoList.add(datasBean.dateStr!!)
            childTodoList[groupTodoList.size - 1].add(datasBean)

        }
        adapter.notifyDataSetChanged()
    }

    fun requestNet(){
        if (status == 0) {
            model.getTodoList(0, page, this)
        } else{
            model.getTodoDoneList(0, page, this)
        }
    }

    fun showDialog(groupPosition: Int, childPosition: Int) {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle("删除Todo")
        builder.setMessage("确定要删除该Todo?")
        builder.setPositiveButton("确定", { dialogInterface, i ->
            model.deleteTodo(childTodoList[groupPosition][childPosition].id, this)
            childTodoList[groupPosition].removeAt(childPosition) //里层数据删除一条数据
            if (childTodoList[groupPosition].size == 0) { //如果里层没有数据了
                childTodoList.removeAt(groupPosition)
                groupTodoList.removeAt(groupPosition)    //外层移除一条数据
            }
            adapter.notifyDataSetChanged() //通知刷新
        })
        builder.setNegativeButton("取消", { dialogInterface, i ->

        })
        /***
         * 设置点击返回键不会消失
         * */
        builder.setCancelable(true).create()
        builder.show()

    }

    override fun loadSuccess(o: Any) {
        if (o is TodoListBean) {
            val todoListBean = o
            val datas = todoListBean.data!!.datas
            val group = ArrayList<String>()
            val child = ArrayList<MutableList<TodoListBean.DataBean.DatasBean>>()
            //        按日期分类
            var date = datas!!.get(0).dateStr
            val sameList = ArrayList<TodoListBean.DataBean.DatasBean>()
            child.clear()
            for (i in datas.indices) {
                if (date == datas.get(i).dateStr) {
                    sameList.add(datas.get(i))

                } else {
                    val tempList = ArrayList<TodoListBean.DataBean.DatasBean>()
                    tempList.addAll(sameList)
                    child.add(tempList)
                    group.add(tempList[0].dateStr!!)
                    groupTodoList.clear()
                    groupTodoList.addAll(group)
                    childTodoList.clear()
                    childTodoList.addAll(child)
                    sameList.clear()
                    date = datas.get(i).dateStr
                    sameList.add(datas.get(i))
                }
                if (i == datas.size - 1) {
                    val tempList = java.util.ArrayList<TodoListBean.DataBean.DatasBean>()
                    tempList.addAll(sameList)
                    child.add(tempList)
                    group.add(tempList[0].dateStr!!)
                    groupTodoList.clear()
                    groupTodoList.addAll(group)
                    childTodoList.clear()
                    childTodoList.addAll(child)
                }
            }
            adapter.notifyDataSetChanged()
        }
        if (o is LinkedTreeMap<*, *>) {
            LiveDataBus.get().with("updateTodo").value = childTodoList[groupPosition][childPosition]
            childTodoList[groupPosition].removeAt(childPosition)//更新完后移除一条代办
            if (childTodoList[groupPosition].size == 0) {//如果该里层没有数据了，移除外层的数据
                groupTodoList.removeAt(groupPosition)
            }
            adapter.notifyDataSetChanged()
        }
    }

    override fun loadFailure(message: String) {
    }

    override fun loadStart() {
    }

    override fun loadComplete() {
    }

    override fun deleteTodo(groupPosition: Int, childPosition: Int) {
        showDialog(groupPosition, childPosition)
    }

    override fun updateTodo(groupPosition: Int, childPosition: Int) {
        this.groupPosition = groupPosition
        this.childPosition = childPosition
        val mStatus: Int
        if (status == 0) {
            mStatus = 1
        } else {
            mStatus = 0
        }
        model.updateTodo(childTodoList[groupPosition][childPosition].id, childTodoList[groupPosition][childPosition].title!!,
                childTodoList[groupPosition][childPosition].content!!, childTodoList[groupPosition][childPosition].dateStr!!,
                mStatus, childTodoList[groupPosition][childPosition].type, this)
    }

    override fun itemClick(groupPosition: Int, childPosition: Int) {
        val intent = Intent(context, TodoDetailActivity::class.java)
        intent.putExtra("mode", status + 1)
        intent.putExtra("title", childTodoList[groupPosition][childPosition].title)
        intent.putExtra("content", childTodoList[groupPosition][childPosition].content)
        intent.putExtra("id", childTodoList[groupPosition][childPosition].id)
        intent.putExtra("date", childTodoList[groupPosition][childPosition].dateStr)
        intent.putExtra("status", childTodoList[groupPosition][childPosition].status)
        activity!!.startActivityForResult(intent, 1)
    }
}