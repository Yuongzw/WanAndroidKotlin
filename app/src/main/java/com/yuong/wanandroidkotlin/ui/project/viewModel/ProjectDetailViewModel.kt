package com.yuong.wanandroidkotlin.ui.project.viewModel

import android.content.Intent
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.yuong.wanandroidkotlin.MyConstans
import com.yuong.wanandroidkotlin.base.BaseViewModel
import com.yuong.wanandroidkotlin.bean.ProjectDetailBean
import com.yuong.wanandroidkotlin.bean.WanAndroidBaseResponse
import com.yuong.wanandroidkotlin.databinding.FragmentProjectDetailBinding
import com.yuong.wanandroidkotlin.livedatabus.LiveDataBus
import com.yuong.wanandroidkotlin.net.BaseLoadListener
import com.yuong.wanandroidkotlin.ui.adapter.ProjectRvAdapter
import com.yuong.wanandroidkotlin.ui.login.ui.LoginActivity
import com.yuong.wanandroidkotlin.ui.project.model.ProjectModel
import com.yuong.wanandroidkotlin.ui.project.model.ProjectModelImpl
import com.yuong.wanandroidkotlin.util.ToastUtils
import com.yuong.wanandroidkotlin.widget.EmptyLayout

class ProjectDetailViewModel : BaseViewModel<FragmentProjectDetailBinding>(), BaseLoadListener<Any>, ProjectRvAdapter.OnCollectListener {

    private lateinit var projectModel: ProjectModel
    var cid = 0
    private var currentPage = 0
    private var isRefresh = false
    private var isFirst = true
    private var isCollected: Boolean = false
    private val itemList = ArrayList<ProjectDetailBean.DataBean.DatasBean>()
    private lateinit var adapter: ProjectRvAdapter
    private var position: Int = 0

    override fun initUi() {
        initRecyclerView()
        initListener()
    }


    private fun initRecyclerView() {
        adapter = ProjectRvAdapter(activity!!)
        binding!!.projectRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding!!.projectRecyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        binding!!.projectRecyclerView.adapter = adapter

        binding!!.projectRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        binding!!.projectDetailEmptyLayout.setOnLayoutClickListener(listener = View.OnClickListener { view ->
            isFirst = true
            isRefresh = true
            binding!!.projectDetailEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING)
            //清除列表
            currentPage = 0
            projectModel.loadProjectDetailData(currentPage, cid , this@ProjectDetailViewModel)
        })

        binding!!.projectSmartRefreshLayout.setOnRefreshListener(OnRefreshListener { refreshLayout ->
            isRefresh = true
            //清除列表
            adapter.data.clear()
            itemList.clear()
            adapter.notifyDataSetChanged()
            binding!!.projectSmartRefreshLayout.setNoMoreData(false)
            currentPage = 0
            projectModel.loadProjectDetailData(currentPage, cid, this@ProjectDetailViewModel)
        })

        binding!!.projectSmartRefreshLayout.setOnLoadMoreListener { refreshLayout ->
            isRefresh = false
            projectModel.loadProjectDetailData(++currentPage, cid, this@ProjectDetailViewModel)
        }
    }

    override fun initNet() {
        projectModel = ProjectModelImpl(activity!!.applicationContext)
        projectModel.loadProjectDetailData(currentPage, cid, this)
    }

    fun collectArticle(id: Int, position: Int) {
        this.position = position
        if (!MyConstans.ISLOGIN) {
            activity!!.startActivity(Intent(activity, LoginActivity::class.java))
            ToastUtils.showShort("您还没有登录！")
            return
        }
        isCollected = true
        projectModel.collectArticle(id, this)
    }

    fun cancelCollectArticle(id: Int, position: Int) {
        this.position = position
        if (!MyConstans.ISLOGIN) {
            activity!!.startActivity(Intent(activity, LoginActivity::class.java))
            ToastUtils.showShort("您还没有登录！")
            return
        }
        isCollected = false
        projectModel.cancelCollected(id, this)
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
                    binding!!.projectSmartRefreshLayout.finishRefresh()
                } else {
                    binding!!.projectSmartRefreshLayout.finishLoadMoreWithNoMoreData()
                }
            } else {
                binding!!.projectDetailEmptyLayout.setErrorType(EmptyLayout.NODATA)
            }
        } else if (o is ProjectDetailBean) {
            val projectDetailBean = o
            if (!isFirst) {
                if (isRefresh) {
                    binding!!.projectSmartRefreshLayout.finishRefresh()
                } else {
                    binding!!.projectSmartRefreshLayout.finishLoadMore()
                }
            } else {
                if (projectDetailBean.data!!.datas != null && projectDetailBean.data!!.datas!!.size > 0) {
                    isFirst = false
                    binding!!.projectDetailEmptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT)
                    binding!!.projectDetailContent.visibility = View.VISIBLE
                } else {
                    binding!!.projectDetailEmptyLayout.setErrorType(EmptyLayout.NODATA)
                }
            }
            if (projectDetailBean.data!!.datas != null && projectDetailBean.data!!.datas!!.size > 0) {
                for (datasBean in projectDetailBean.data!!.datas!!) {
                    if (MyConstans.collectIds.size > 0 && MyConstans.collectIds.contains(datasBean.id)) {
                        datasBean.isCollected = true
                    } else {
                        datasBean.isCollected = false
                    }
                    datasBean.currentPage = 3
                    itemList.add(datasBean)
                }
//                itemList.addAll(projectDetailBean.data!!.datas!!)
                adapter.setNewData(itemList)
                binding!!.projectSmartRefreshLayout.setNoMoreData(false)
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
            }else if (response.errorCode == -1001){
                ToastUtils.showShort(response.errorMsg)
                activity!!.startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
    }

    override fun loadFailure(message: String) {
        if (!isFirst) {
            if (isRefresh) {
                binding!!.projectSmartRefreshLayout.finishRefresh()
            } else {
                binding!!.projectSmartRefreshLayout.finishLoadMore()
            }
        } else {
            binding!!.projectDetailEmptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR)
        }
    }

    override fun loadStart() {
    }

    override fun loadComplete() {
    }
}