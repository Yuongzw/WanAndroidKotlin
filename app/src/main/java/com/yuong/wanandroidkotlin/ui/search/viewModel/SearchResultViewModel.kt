package com.yuong.wanandroidkotlin.ui.search.viewModel

import android.content.Context
import android.content.Intent
import android.databinding.ObservableField
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.yuong.wanandroidkotlin.MyConstans
import com.yuong.wanandroidkotlin.base.BaseViewModel
import com.yuong.wanandroidkotlin.bean.SearchResultBean
import com.yuong.wanandroidkotlin.bean.WanAndroidBaseResponse
import com.yuong.wanandroidkotlin.databinding.ActivitySearchResultBinding
import com.yuong.wanandroidkotlin.livedatabus.LiveDataBus
import com.yuong.wanandroidkotlin.net.BaseLoadListener
import com.yuong.wanandroidkotlin.ui.adapter.MyRvAdapter
import com.yuong.wanandroidkotlin.ui.adapter.MySearchRvAdapter
import com.yuong.wanandroidkotlin.ui.login.ui.LoginActivity
import com.yuong.wanandroidkotlin.ui.search.model.SearchModel
import com.yuong.wanandroidkotlin.ui.search.model.SearchModelImpl
import com.yuong.wanandroidkotlin.ui.search.ui.SearchResultActivity
import com.yuong.wanandroidkotlin.util.ToastUtils
import com.yuong.wanandroidkotlin.widget.EmptyLayout

class SearchResultViewModel : BaseViewModel<ActivitySearchResultBinding>(), BaseLoadListener<Any> {
    private lateinit var model: SearchModel
    //关键字
    var key = ObservableField("")
    var keyWord: String = ""
    private var page = 0
    private var isFirst: Boolean = true
    private var isRefresh = false
    private var isCollected: Boolean = false
    private var position: Int = 0
    var dataList: MutableList<SearchResultBean.DataBean.DatasBean> = ArrayList()
    private lateinit var adapter: MySearchRvAdapter

    override fun initUi() {
        binding!!.tvSearchResult.setOnClickListener { view ->
            hideKeyboard()
            keyWord = key.get()!!
            if (keyWord.length <= 0) {
                ToastUtils.showShort("输入不能为空")
                return@setOnClickListener
            }
            dataList.clear()
            isFirst = true
            page = 0
            binding!!.searchResultSmartRefreshLayout.setNoMoreData(false)
            binding!!.searchResultContent.visibility = View.GONE
            binding!!.searchResultEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING)
            model.loadSearchData(page, keyWord, this)
        }

        binding!!.searchResultSmartRefreshLayout.setOnRefreshListener { refreshLayout ->
            dataList.clear()
            isRefresh = true
            page = 0
            binding!!.searchResultSmartRefreshLayout.setNoMoreData(false)
            model.loadSearchData(page, keyWord, this)

        }

        binding!!.searchResultSmartRefreshLayout.setOnLoadMoreListener { refreshLayout ->
            isRefresh = false
            model.loadSearchData(++page, keyWord, this)
        }

        binding!!.searchResultEmptyLayout.setOnLayoutClickListener(View.OnClickListener { view ->
            dataList.clear()
            binding!!.searchResultContent.visibility = View.GONE
            binding!!.searchResultEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING)
            model.loadSearchData(page, keyWord, this)
        })

        initRecyclerView()
        initNet()
    }

    private fun initRecyclerView() {
        adapter = MySearchRvAdapter(activity!!)
        binding!!.searchResultRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding!!.searchResultRecyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        binding!!.searchResultRecyclerView.adapter = adapter

    }

    fun hideKeyboard() {
        val manager = binding!!.searchResultEditText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(binding!!.searchResultEditText.windowToken, 0)
    }

    fun collectArticle(id: Int, position: Int) {
        this.position = position
        if (!MyConstans.ISLOGIN) {
//            ARouter.getInstance().build(RouterActivityPath.Sign.PAGER_LOGIN).navigation()
            return
        }
        isCollected = true
        model.collectArticle(id, this)
    }

    fun cancelCollectArticle(id: Int, position: Int) {
        this.position = position
        if (!MyConstans.ISLOGIN) {
//            ARouter.getInstance().build(RouterActivityPath.Sign.PAGER_LOGIN).navigation()
            return
        }
        isCollected = false
        model.cancelCollected(id, this)
    }

    override fun initNet() {
        model = SearchModelImpl(activity!!.applicationContext)
        model.loadSearchData(page, keyWord, this)
    }

    override fun loadSuccess(o: Any) {
        if (o is Int) {
            if (!isFirst) {
                if (isRefresh) {
                    binding!!.searchResultSmartRefreshLayout.finishRefresh()
                } else {
                    binding!!.searchResultSmartRefreshLayout.finishLoadMoreWithNoMoreData()
                }
            } else {
                binding!!.searchResultEmptyLayout.setErrorType(EmptyLayout.NODATA)
            }
        } else if (o is List<*>) {
            val datasBeans = o as List<SearchResultBean.DataBean.DatasBean>
            if (!isFirst) {
                if (isRefresh) {
                    binding!!.searchResultSmartRefreshLayout.finishRefresh()
                } else {
                    binding!!.searchResultSmartRefreshLayout.finishLoadMore()
                }
            } else {
                if (datasBeans.size > 0) {
                    isFirst = false
                    binding!!.searchResultEmptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT)
                    binding!!.searchResultContent.visibility = View.VISIBLE
                } else {
                    binding!!.searchResultEmptyLayout.setErrorType(EmptyLayout.NODATA)
                }
            }
            if (datasBeans.size > 0) {
                for (datasBean in datasBeans) {
                    if (MyConstans.collectIds.size > 0 && MyConstans.collectIds.contains(datasBean.id)) {
                        datasBean.isCollected = true
                    } else {
                        datasBean.isCollected = false
                    }
                    dataList.add(datasBean)
                }
                adapter.setNewData(dataList)
                binding!!.searchResultSmartRefreshLayout.setNoMoreData(false)

            }
        } else if (o is WanAndroidBaseResponse) {
            val response = o
            if (response.errorCode == 0) {
                if (isCollected) {
                    ToastUtils.showShort("收藏成功！")
                } else {
                    ToastUtils.showShort("取消收藏成功！")
                }
                dataList.get(position).isCollected = isCollected
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
                binding!!.searchResultSmartRefreshLayout.finishRefresh()
            } else {
                binding!!.searchResultSmartRefreshLayout.finishLoadMore()
            }
        } else {
            binding!!.searchResultEmptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR)
        }
    }

    override fun loadStart() {
    }

    override fun loadComplete() {
    }
}