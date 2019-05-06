package com.yuong.wanandroidkotlin.ui.tree.viewModel

import android.content.Intent
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.yuong.wanandroidkotlin.MyConstans
import com.yuong.wanandroidkotlin.base.BaseViewModel
import com.yuong.wanandroidkotlin.bean.HomeBean
import com.yuong.wanandroidkotlin.bean.WanAndroidBaseResponse
import com.yuong.wanandroidkotlin.databinding.ActivityTreeArticleBinding
import com.yuong.wanandroidkotlin.net.BaseLoadListener
import com.yuong.wanandroidkotlin.ui.adapter.MyRvAdapter
import com.yuong.wanandroidkotlin.ui.login.ui.LoginActivity
import com.yuong.wanandroidkotlin.ui.main.model.TreeModel
import com.yuong.wanandroidkotlin.ui.main.model.TreeModelImpl
import com.yuong.wanandroidkotlin.util.ToastUtils
import com.yuong.wanandroidkotlin.widget.EmptyLayout
import java.util.ArrayList

class TreeArticleViewModel: BaseViewModel<ActivityTreeArticleBinding>(), BaseLoadListener<Any>, MyRvAdapter.OnCollectListener {

    private lateinit var model: TreeModel
    private lateinit var adapter: MyRvAdapter
    var cid = 0
    private var currentPage = 0
    private var isRefresh = false
    private var isFirst = true
    private var isCollected: Boolean = false
    private val itemList = ArrayList<HomeBean.DataBean.DatasBean>()
    private var position: Int = 0

    override fun initUi() {
        initRecyclerView()
        initListener()
        initNet()
    }

    private fun initRecyclerView() {
        adapter = MyRvAdapter(activity!!)
        binding!!.treeArticleRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding!!.treeArticleRecyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        binding!!.treeArticleRecyclerView.adapter = adapter
        adapter.collectListener = this
    }

    private fun initListener() {
        binding!!.treeArticleEmptyLayout.setOnLayoutClickListener(listener = View.OnClickListener { view ->
            isFirst = true
            isRefresh = true
            binding!!.treeArticleEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING)
            //清除列表
            currentPage = 0
            model.loadTreeArticleData(currentPage , cid, this@TreeArticleViewModel)
        })

        binding!!.treeArticleSmartRefreshLayout.setOnRefreshListener(OnRefreshListener { refreshLayout ->
            isRefresh = true
            //清除列表
            adapter.data.clear()
            itemList.clear()
            adapter.notifyDataSetChanged()
            binding!!.treeArticleSmartRefreshLayout.setNoMoreData(false)
            currentPage = 0
            model.loadTreeArticleData(currentPage , cid, this@TreeArticleViewModel)
        })

        binding!!.treeArticleSmartRefreshLayout.setOnLoadMoreListener { refreshLayout ->
            isRefresh = false
            model.loadTreeArticleData( ++currentPage, cid, this@TreeArticleViewModel)
        }
    }

    override fun initNet() {
        model = TreeModelImpl(activity!!.applicationContext)
        model.loadTreeArticleData(currentPage, cid, this)
    }

    fun collectArticle(id: Int, position: Int) {
        this.position = position
        if (!MyConstans.ISLOGIN) {
            activity!!.startActivity(Intent(activity, LoginActivity::class.java))
            ToastUtils.showShort("您还没有登录！")
            return
        }
        isCollected = true
        model.collectArticle(id, this)
    }

    fun cancelCollectArticle(id: Int, position: Int) {
        this.position = position
        if (!MyConstans.ISLOGIN) {
            activity!!.startActivity(Intent(activity, LoginActivity::class.java))
            ToastUtils.showShort("您还没有登录！")
            return
        }
        isCollected = false
        model.cancelCollected(id, this)
    }

    override fun collect(id: Int, position: Int) {
        collectArticle(id, position)
    }

    override fun cancelCollect(id: Int, position: Int) {
        cancelCollectArticle(id, position)
    }



    override fun loadSuccess(o: Any) {
        if (o is Int) {
            if (!isFirst) {
                if (isRefresh) {
                    binding!!.treeArticleSmartRefreshLayout.finishRefresh()
                } else {
                    binding!!.treeArticleSmartRefreshLayout.finishLoadMoreWithNoMoreData()
                }
            } else {
                binding!!.treeArticleEmptyLayout.setErrorType(EmptyLayout.NODATA)
            }
        } else if (o is HomeBean) {
            val dataBean = o
            if (!isFirst) {
                if (isRefresh) {
                    binding!!.treeArticleSmartRefreshLayout.finishRefresh()
                } else {
                    binding!!.treeArticleSmartRefreshLayout.finishLoadMore()
                }
            } else {
                if (dataBean.data!!.datas!!.size > 0) {
                    isFirst = false
                    binding!!.treeArticleEmptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT)
                    binding!!.treeArticleSmartRefreshLayout.visibility = View.VISIBLE
                } else {
                    binding!!.treeArticleEmptyLayout.setErrorType(EmptyLayout.NODATA)
                }
            }
            if (dataBean.data!!.datas!!.size > 0) {
                for (datasBean in dataBean.data!!.datas!!) {
                    if (MyConstans.collectIds.size > 0 && MyConstans.collectIds.contains(datasBean.id)) {
                        datasBean.isCollected = true
                    } else {
                        datasBean.isCollected = false
                    }
                    datasBean.currentPage = 1
                    itemList.add(datasBean)
                }
//                itemList.addAll(dataBean.data!!.datas!!)
                adapter.setNewData(itemList)
                binding!!.treeArticleSmartRefreshLayout.setNoMoreData(false)
            }
        } else if (o is WanAndroidBaseResponse) {
            val response = o
            if (response.errorCode == 0) {
                if (isCollected) {
                    ToastUtils.showShort("收藏成功！")
                } else {
                    ToastUtils.showShort("取消收藏成功！")
                }
                itemList.get(position).isCollected = isCollected
                adapter.notifyItemChanged(position)
            } else if (response.errorCode == -1001){
                ToastUtils.showShort(response.errorMsg)
                activity!!.startActivity(Intent(activity, LoginActivity::class.java))
            }
        }

    }

    override fun loadFailure(message: String) {
        if (!isFirst) {
            if (isRefresh) {
                binding!!.treeArticleSmartRefreshLayout.finishRefresh()
            } else {
                binding!!.treeArticleSmartRefreshLayout.finishLoadMore()
            }
        } else {
            binding!!.treeArticleEmptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR)
        }
    }

    override fun loadStart() {
    }

    override fun loadComplete() {
    }
}