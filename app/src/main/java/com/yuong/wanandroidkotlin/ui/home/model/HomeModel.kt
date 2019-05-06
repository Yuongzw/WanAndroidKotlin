package com.yuong.wanandroidkotlin.ui.home.model

import com.yuong.wanandroidkotlin.net.BaseLoadListener

interface HomeModel {
    /**
     * 获取首页数据
     *
     * @param loadListener
     */
    fun loadHomeData(page: Int, loadListener: BaseLoadListener<Any>)

    fun loadBannerData(loadListener: BaseLoadListener<Any>)

    /**
     * 收藏文章
     * @param id 文章id
     * @param loadListener
     */
    fun collectArticle(id: Int, loadListener: BaseLoadListener<Any>)

    /**
     * 取消收藏文章
     * @param id 文章id
     * @param loadListener
     */
    fun cancelCollected(id: Int, loadListener: BaseLoadListener<Any>)
}