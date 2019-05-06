package com.yuong.wanandroidkotlin.ui.collect.viewModel

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.yuong.wanandroidkotlin.base.BaseViewModel
import com.yuong.wanandroidkotlin.bean.HomeBean
import com.yuong.wanandroidkotlin.bean.WanAndroidBaseResponse
import com.yuong.wanandroidkotlin.databinding.ActivityCollectBinding
import com.yuong.wanandroidkotlin.net.BaseLoadListener
import com.yuong.wanandroidkotlin.ui.adapter.MyCollcetRvAdapter
import com.yuong.wanandroidkotlin.ui.collect.model.CollectModel
import com.yuong.wanandroidkotlin.ui.collect.model.CollectModelImpl
import com.yuong.wanandroidkotlin.util.ToastUtils
import com.yuong.wanandroidkotlin.widget.EmptyLayout
import java.util.ArrayList

class CollectViewModel : BaseViewModel<ActivityCollectBinding>(), BaseLoadListener<Any> {
    private lateinit var model: CollectModel
    private lateinit var adapter: MyCollcetRvAdapter
    private var position: Int = 0
    private var page: Int = 0
    private var isFirst = true
    private var isRefresh = false
    private val datasBeans = ArrayList<HomeBean.DataBean.DatasBean>()

    override fun initUi() {
        initRecyclerView()
        initListener()
        initNet()
    }

    private fun initListener() {
        binding!!.collectSmartRefreshLayout.setOnRefreshListener { refreshLayout ->
            page = 0
            datasBeans.clear()
            isRefresh = true
            model.getCollectArticleData(page, this)
        }

        binding!!.collectSmartRefreshLayout.setOnLoadMoreListener { refreshLayout ->
            isRefresh = false
            model.getCollectArticleData(++page, this)
        }

        binding!!.collectEmptyLayout.setOnLayoutClickListener(View.OnClickListener { view ->
            isFirst = true
            datasBeans.clear()
            model.getCollectArticleData(page, this)
        })
    }

    private fun initRecyclerView() {
        adapter = MyCollcetRvAdapter(activity!!)
        binding!!.collectRv.layoutManager = LinearLayoutManager(activity)
        binding!!.collectRv.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        binding!!.collectRv.adapter = adapter

    }

    override fun initNet() {
        model = CollectModelImpl(activity!!.applicationContext)
        model.getCollectArticleData(page, this)
    }

    fun cancelCollectArticle(id: Int, originId: Int) {
        model.cancelCollectArticle(id, originId, this)
    }

    override fun loadSuccess(o: Any) {
        var articleBean: HomeBean? = null
        if (o is WanAndroidBaseResponse) {
            val reponse = o
            if (reponse.errorCode == -1) {
                ToastUtils.showShort("取消收藏失败！")
            } else {
                ToastUtils.showShort("取消收藏成功！")
                deleteItemForAdapter()
            }
        }
        if (o is HomeBean) {
            articleBean = o
        }

        if (articleBean != null) {
            if (!isFirst) {
                if (isRefresh) {
                    binding!!.collectSmartRefreshLayout.finishRefresh()
                } else {
                    if (articleBean.data!!.datas != null && articleBean.data!!.datas!!.size > 0) {
                        binding!!.collectSmartRefreshLayout.finishLoadMore()
                    } else {
                        binding!!.collectSmartRefreshLayout.finishLoadMoreWithNoMoreData()
                    }
                }
            } else {
                if (articleBean.data!!.datas != null && articleBean.data!!.datas!!.size > 0) {
                    isFirst = false
                    binding!!.collectEmptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT)
                    binding!!.collectSmartRefreshLayout.visibility = View.VISIBLE
                } else {
                    binding!!.collectEmptyLayout.setErrorType(EmptyLayout.NODATA)
                }
            }
            if (articleBean.data!!.datas != null && articleBean.data!!.datas!!.size > 0) {
                adapter.setNewData(articleBean.data!!.datas)
            }

        }
    }

    private fun deleteItemForAdapter() {
        if (datasBeans.size > 0) {
            datasBeans.removeAt(position)
        }
        adapter.data.removeAt(position)
        adapter.notifyDataSetChanged()
    }

    override fun loadFailure(message: String) {
    }

    override fun loadStart() {
    }

    override fun loadComplete() {
    }
}