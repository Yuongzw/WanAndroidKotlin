package com.yuong.wanandroidkotlin.ui.main.model

import com.yuong.wanandroidkotlin.net.BaseLoadListener

interface WxModel {
    /**
     * 获取微信公众号列表
     *
     * @param loadListener
     */
    abstract fun loadWXNumbersData(loadListener: BaseLoadListener<Any>)

    /**
     * 获取某个公众号历史数据
     * @param page
     * @param id
     * @param loadListener
     */
    abstract fun loadWXAriticleData(id: Int, page: Int, loadListener: BaseLoadListener<Any>)

    /**
     * 收藏文章
     * @param id 文章id
     * @param loadListener
     */
    abstract fun collectArticle(id: Int, loadListener: BaseLoadListener<Any>)

    /**
     * 取消收藏文章
     * @param id 文章id
     * @param loadListener
     */
    abstract fun cancelCollected(id: Int, loadListener: BaseLoadListener<Any>)
}