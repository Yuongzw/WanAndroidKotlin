package com.yuong.wanandroidkotlin.ui.main.model

import com.yuong.wanandroidkotlin.net.BaseLoadListener

interface TreeModel {
    /**
     * 获取体系左边栏数据
     *
     * @param loadListener
     */
    fun loadTreeData(loadListener: BaseLoadListener<Any>)

    /**
     * 获取体系下的文章
     * @param page
     * @param cid
     * @param loadListener
     */
    fun loadTreeArticleData(page: Int, cid: Int, loadListener: BaseLoadListener<Any>)

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