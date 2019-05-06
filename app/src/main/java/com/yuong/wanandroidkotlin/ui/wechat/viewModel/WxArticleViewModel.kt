package com.yuong.wanandroidkotlin.ui.wechat.viewModel

import android.content.Intent
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.yuong.wanandroidkotlin.MyConstans
import com.yuong.wanandroidkotlin.base.BaseViewModel
import com.yuong.wanandroidkotlin.bean.HomeBean
import com.yuong.wanandroidkotlin.bean.WXArticleBean
import com.yuong.wanandroidkotlin.bean.WanAndroidBaseResponse
import com.yuong.wanandroidkotlin.databinding.FragmentWxArticleBinding
import com.yuong.wanandroidkotlin.livedatabus.LiveDataBus
import com.yuong.wanandroidkotlin.net.BaseLoadListener
import com.yuong.wanandroidkotlin.ui.adapter.MyRvAdapter
import com.yuong.wanandroidkotlin.ui.login.ui.LoginActivity
import com.yuong.wanandroidkotlin.ui.main.model.WxModel
import com.yuong.wanandroidkotlin.ui.main.model.WxModelImpl
import com.yuong.wanandroidkotlin.util.ToastUtils
import com.yuong.wanandroidkotlin.widget.EmptyLayout
import java.util.ArrayList

class WxArticleViewModel : BaseViewModel<FragmentWxArticleBinding>(), BaseLoadListener<Any>, MyRvAdapter.OnCollectListener {

    private lateinit var model: WxModel
    private lateinit var adapter: MyRvAdapter
    var id = 0
    private var currentPage = 0
    private var isRefresh = false
    private var isFirst = true
    private var isCollected: Boolean = false
    private val itemList = ArrayList<HomeBean.DataBean.DatasBean>()
    private var position: Int = 0

    override fun initUi() {
        initRecyclerView()
        initListener()
    }

    private fun initRecyclerView() {
        adapter = MyRvAdapter(activity!!)
        binding!!.wxArticleRv.layoutManager = LinearLayoutManager(activity)
        binding!!.wxArticleRv.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        binding!!.wxArticleRv.adapter = adapter

        binding!!.wxArticleRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 50) {//向下滚动
                    LiveDataBus.get().with("bottom").value = false
                } else if (dy < -50) {
                    LiveDataBus.get().with("bottom").value = true
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
        adapter.collectListener = this
    }


    private fun initListener() {
        binding!!.wxArticleEmptyLayout.setOnLayoutClickListener(listener = View.OnClickListener { view ->
            isFirst = true
            isRefresh = true
            binding!!.wxArticleEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING)
            //清除列表
            currentPage = 0
            model.loadWXAriticleData(id, currentPage, this@WxArticleViewModel)
        })

        binding!!.wxArticleSmartRefreshLayout.setOnRefreshListener(OnRefreshListener { refreshLayout ->
            isRefresh = true
            //清除列表
            adapter.data.clear()
            itemList.clear()
            adapter.notifyDataSetChanged()
            binding!!.wxArticleSmartRefreshLayout.setNoMoreData(false)
            currentPage = 0
            model.loadWXAriticleData(id, currentPage, this@WxArticleViewModel)
        })

        binding!!.wxArticleSmartRefreshLayout.setOnLoadMoreListener { refreshLayout ->
            isRefresh = false
            model.loadWXAriticleData(id, ++currentPage, this@WxArticleViewModel)
        }
    }


    override fun initNet() {
        model = WxModelImpl(activity!!.applicationContext)
        model.loadWXAriticleData(id, currentPage, this)
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
        if (o is Int) run {
            if (!isFirst) {
                if (isRefresh) {
                    binding!!.wxArticleSmartRefreshLayout.finishRefresh()
                } else {
                    binding!!.wxArticleSmartRefreshLayout.finishLoadMoreWithNoMoreData()
                }
            } else {
                binding!!.wxArticleEmptyLayout.setErrorType(EmptyLayout.NODATA)
            }
        } else if (o is HomeBean) run {
            val homeBean = o
            if (!isFirst) {
                if (isRefresh) {
                    binding!!.wxArticleSmartRefreshLayout.finishRefresh()
                } else {
                    binding!!.wxArticleSmartRefreshLayout.finishLoadMore()
                }
            } else {
                if (homeBean.data!!.datas != null && homeBean.data!!.datas!!.size > 0) {
                    isFirst = false
                    binding!!.wxArticleEmptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT)
                    binding!!.wxArticleContent.visibility = View.VISIBLE

                } else {
                    binding!!.wxArticleEmptyLayout.setErrorType(EmptyLayout.NODATA)
                }
            }
            for (dataBean in homeBean.data!!.datas!!) {
                if (MyConstans.collectIds.size > 0 && MyConstans.collectIds.contains(dataBean.id)) {
                    dataBean.isCollected = true
                } else {
                    dataBean.isCollected = false
                }
                dataBean.currentPage = 2
                itemList.add(dataBean)
            }
//                itemList.addAll(homeBean.data!!.datas!!)
            adapter.setNewData(itemList)
            binding!!.wxArticleSmartRefreshLayout.setNoMoreData(false)
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
            } else if (response.errorCode == -1001) {
                ToastUtils.showShort(response.errorMsg)
                activity!!.startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
    }

    override fun loadFailure(message: String) {
        if (!isFirst) {
            if (isRefresh) {
                binding!!.wxArticleSmartRefreshLayout.finishRefresh()
            } else {
                binding!!.wxArticleSmartRefreshLayout.finishLoadMore()
            }
        } else {
            binding!!.wxArticleEmptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR)
        }
    }

    override fun loadStart() {
    }

    override fun loadComplete() {
    }
}