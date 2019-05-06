package com.yuong.wanandroidkotlin.ui.tree.viewModel

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.yuong.wanandroidkotlin.ui.adapter.TreeLeftAdapter
import com.yuong.wanandroidkotlin.ui.adapter.TreeRightAdapter
import com.yuong.wanandroidkotlin.base.BaseViewModel
import com.yuong.wanandroidkotlin.bean.TreeBean
import com.yuong.wanandroidkotlin.databinding.FragmentTreeBinding
import com.yuong.wanandroidkotlin.livedatabus.LiveDataBus
import com.yuong.wanandroidkotlin.net.BaseLoadListener
import com.yuong.wanandroidkotlin.ui.main.model.TreeModel
import com.yuong.wanandroidkotlin.ui.main.model.TreeModelImpl
import com.yuong.wanandroidkotlin.ui.main.model.WxModelImpl
import com.yuong.wanandroidkotlin.widget.EmptyLayout

class TreeViewModel : BaseViewModel<FragmentTreeBinding>(), BaseLoadListener<Any> {


    private lateinit var model: TreeModel
    private lateinit var leftAdapter: TreeLeftAdapter
    private lateinit var rightAdapter: TreeRightAdapter

    override fun initUi() {
        initRecyclerView()
        binding!!.treeEmptyLayout.setOnLayoutClickListener(View.OnClickListener {
            binding!!.treeEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING)
            model.loadTreeData(this)
        })
    }

    private fun initRecyclerView() {
        leftAdapter = TreeLeftAdapter(activity!!)
        binding!!.treeRvLeft.layoutManager = LinearLayoutManager(activity)
        binding!!.treeRvLeft.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        binding!!.treeRvLeft.adapter = leftAdapter
        rightAdapter = TreeRightAdapter(activity!!)
        binding!!.treeRvRight.layoutManager = LinearLayoutManager(activity)
        binding!!.treeRvRight.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        binding!!.treeRvRight.adapter = rightAdapter
        binding!!.treeRvLeft.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 50) {//向下滚动
                    LiveDataBus.get().with("bottom").value = false
                } else if (dy < -50) {
                    LiveDataBus.get().with("bottom").value = true
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

    }

    override fun initNet() {
        model = TreeModelImpl(activity!!.applicationContext)
        model.loadTreeData(this)
    }

    fun refreshRight(t: TreeBean.DataBean?) {
        rightAdapter.setNewData(t!!.children)
    }

    override fun loadSuccess(t: Any) {
        if (t is TreeBean) run {
            val treeBean = t
            if (treeBean.data != null && treeBean.data!!.size > 0) {
                leftAdapter.setNewData(treeBean.data)
                binding!!.treeEmptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT)
                binding!!.treeContent.visibility = View.VISIBLE
                for (i in 0 until treeBean.data!!.size) {
                    if (i == 0) {
                        rightAdapter.setNewData(treeBean.data!!.get(0).children)
                    }
                }
            }
        }
    }

    override fun loadFailure(message: String) {
        binding!!.treeEmptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR)
    }

    override fun loadStart() {
    }

    override fun loadComplete() {
    }
}