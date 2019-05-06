package com.yuong.wanandroidkotlin.ui.project.model

import com.yuong.wanandroidkotlin.net.BaseLoadListener

interface ProjectModel {
    /**
     * 获取项目分类数据
     *
     * @param loadListener
     */
    abstract fun loadProjectCategotyData(loadListener: BaseLoadListener<Any>)

    /**
     * 获取项目详情数据
     * @param page
     * @param cid
     * @param loadListener
     */
    abstract fun loadProjectDetailData(page: Int, cid: Int, loadListener: BaseLoadListener<Any>)

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