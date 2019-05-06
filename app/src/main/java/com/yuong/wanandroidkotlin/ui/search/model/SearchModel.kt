package com.yuong.wanandroidkotlin.ui.search.model

import com.yuong.wanandroidkotlin.net.BaseLoadListener

interface SearchModel {
    /**
     * 获取搜索热词数据
     *
     * @param loadListener
     */
    fun loadHotKeyData(loadListener: BaseLoadListener<Any>)

    /**
     * 获取搜索数据
     * @param page
     * @param keyWord
     * @param loadListener
     */
    fun loadSearchData(page: Int, keyWord: String, loadListener: BaseLoadListener<Any>)


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