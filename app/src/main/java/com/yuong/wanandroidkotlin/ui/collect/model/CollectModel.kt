package com.yuong.wanandroidkotlin.ui.collect.model

import com.yuong.wanandroidkotlin.net.BaseLoadListener

interface CollectModel {

    fun getCollectArticleData(page: Int, loadListener: BaseLoadListener<Any>)

    fun cancelCollectArticle(id: Int, originId: Int, loadListener: BaseLoadListener<Any>)
}