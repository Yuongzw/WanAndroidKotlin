package com.yuong.wanandroidkotlin.ui.home.viewModel

import android.content.Intent
import android.media.Image
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import cn.bingoogolapple.bgabanner.BGABanner
import com.bumptech.glide.Glide
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.thefinestartist.finestwebview.FinestWebView
import com.yuong.wanandroidkotlin.MyConstans
import com.yuong.wanandroidkotlin.ui.adapter.MyRvAdapter
import com.yuong.wanandroidkotlin.base.BaseViewModel
import com.yuong.wanandroidkotlin.bean.BannerBean
import com.yuong.wanandroidkotlin.bean.HomeBean
import com.yuong.wanandroidkotlin.bean.WanAndroidBaseResponse
import com.yuong.wanandroidkotlin.databinding.FragmentHomeBinding
import com.yuong.wanandroidkotlin.ui.home.model.HomeModel
import com.yuong.wanandroidkotlin.ui.home.model.HomeModelImpl
import com.yuong.wanandroidkotlin.net.BaseLoadListener
import com.yuong.wanandroidkotlin.util.ToastUtils
import com.yuong.wanandroidkotlin.widget.EmptyLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.livedatabus.LiveDataBus
import com.yuong.wanandroidkotlin.ui.login.ui.LoginActivity
import java.text.FieldPosition


class HomeViewModel : BaseViewModel<FragmentHomeBinding>(), BaseLoadListener<Any>, MyRvAdapter.OnCollectListener {

    private lateinit var homeModel: HomeModel
    private lateinit var adapter: MyRvAdapter
    lateinit var bannerList: List<BannerBean.DataBean>
    var homeDataList: MutableList<HomeBean.DataBean.DatasBean> = ArrayList()
    private var currentPage = 0
    private var isRefresh = false
    private var isFirst = false
    private var isCollected: Boolean = false
    private var position: Int = 0

    override fun initUi() {
        initRecyclerView()
        initListener()
        homeModel = HomeModelImpl(activity!!.applicationContext)
        binding!!.homeEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING)
    }

    private fun initRecyclerView() {
        adapter = MyRvAdapter(activity!!)
        binding!!.homeRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding!!.homeRecyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        binding!!.homeRecyclerView.adapter = adapter

        binding!!.homeRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        binding!!.homeEmptyLayout.setOnLayoutClickListener(listener = View.OnClickListener { view ->
            isFirst = true
            isRefresh = true
            binding!!.homeEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING)
            //清除列表
            homeDataList.clear()
            currentPage = 0
            homeModel.loadHomeData(currentPage, this@HomeViewModel)
            homeModel.loadBannerData(this)
        })

        binding!!.homeSmartRefreshLayout.setOnRefreshListener(OnRefreshListener { refreshLayout ->
            isRefresh = true
            //清除列表
            homeDataList.clear()
            binding!!.homeSmartRefreshLayout.setNoMoreData(false)
            currentPage = 0
            homeModel.loadHomeData(currentPage, this@HomeViewModel)
        })

        binding!!.homeSmartRefreshLayout.setOnLoadMoreListener { refreshLayout ->
            isRefresh = false
            homeModel.loadHomeData(++currentPage, this@HomeViewModel)
        }
    }

    override fun initNet() {
        isFirst = true
        homeModel.loadHomeData(currentPage, this)
        homeModel.loadBannerData(this)
    }

    fun collectArticle(id: Int, position: Int) {
        this.position = position
        if (!MyConstans.ISLOGIN) {
            activity!!.startActivity(Intent(activity, LoginActivity::class.java))
            ToastUtils.showShort("您还没有登录！")
            return
        }
        isCollected = true
        homeModel.collectArticle(id, this)
    }

    fun cancelCollectArticle(id: Int, position: Int) {
        this.position = position
        if (!MyConstans.ISLOGIN) {
            activity!!.startActivity(Intent(activity, LoginActivity::class.java))
            ToastUtils.showShort("您还没有登录！")
            return
        }
        isCollected = false
        homeModel.cancelCollected(id, this)
    }

    override fun collect(id: Int, position: Int) {
        collectArticle(id, position)
    }

    override fun cancelCollect(id: Int, position: Int) {
        cancelCollectArticle(id, position)
    }


    private fun setBanner(bannerBean: BannerBean) {
        binding!!.bgaBanner.setAdapter({ banner, itemView, model, position ->
            Glide.with(activity!!)
                    .load(model)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .error(R.drawable.default_project_img)
                    .into(itemView as ImageView)
        })
        if (bannerList.size > 0) {
            val images = ArrayList<String>()
            val titles = ArrayList<String>()
            for (dataBean in bannerList) {
                images.add(dataBean.imagePath!!)
                titles.add(dataBean.title!!)
            }
            binding!!.bgaBanner.setData(images, titles)
        }

        binding!!.bgaBanner.setDelegate({ banner, itemView, model, position ->
            FinestWebView.Builder(activity!!).show(bannerBean.data!!.get(position).url!!)//点击条目到文章详情页面
        })
    }

    override fun loadSuccess(o: Any) {
        if (o is Int) run {
            if (!isFirst) {
                if (isRefresh) {
                    binding!!.homeSmartRefreshLayout.finishLoadMore()
                } else {
                    binding!!.homeSmartRefreshLayout.finishLoadMoreWithNoMoreData()
                }
            } else {
                binding!!.homeEmptyLayout.setErrorType(EmptyLayout.NODATA)
            }
        } else if (o is HomeBean) run {
            val homeBean = o
            if (!isFirst) {
                if (isRefresh) {
                    binding!!.homeSmartRefreshLayout.finishRefresh()
                } else {
                    binding!!.homeSmartRefreshLayout.finishLoadMore()
                }
            } else {
                if (homeBean.data!!.datas != null && homeBean.data!!.datas!!.size > 0) {
                    isFirst = false
                    binding!!.homeEmptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT)
                    binding!!.llContent.visibility = View.VISIBLE
                } else {
                    binding!!.homeEmptyLayout.setErrorType(EmptyLayout.NODATA)
                }
            }
            for (dataBean in homeBean.data!!.datas!!) {
                if (MyConstans.collectIds.size > 0 && MyConstans.collectIds.contains(dataBean.id)) {
                    dataBean.isCollected = true
                } else {
                    dataBean.isCollected = false
                }
                dataBean.currentPage = 0
                homeDataList.add(dataBean)
            }
            binding!!.homeSmartRefreshLayout.setNoMoreData(false)
//            homeDataList.addAll(homeBean.data!!.datas!!)
            adapter.setNewData(homeDataList)


        } else if (o is BannerBean) run {
            val bannerBean = o
            if (bannerBean.data != null && bannerBean.data!!.size > 0) {
                bannerList = bannerBean.data!!
                setBanner(bannerBean)
            }
        } else if (o is WanAndroidBaseResponse) {
            val response = o
            if (response.errorCode == 0) {
                if (isCollected) {
                    ToastUtils.showShort("收藏成功！")
                } else {
                    ToastUtils.showShort("取消收藏成功！")
                }
                homeDataList.get(position).isCollected = isCollected
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
                binding!!.homeSmartRefreshLayout.finishRefresh()
            } else {
                binding!!.homeSmartRefreshLayout.finishLoadMore()
            }
        } else {
            binding!!.homeEmptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR)
        }
    }

    override fun loadStart() {
    }

    override fun loadComplete() {
    }

}