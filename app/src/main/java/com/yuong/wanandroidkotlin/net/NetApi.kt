package com.yuong.wanandroidkotlin.net

import com.yuong.wanandroidkotlin.bean.*
import io.reactivex.Observable
import retrofit2.http.*

interface NetApi {

    @GET("article/list/{page}/json")
    fun getHomeData(@Path("page") page: Int): Observable<HomeBean>

    @GET("banner/json")
    fun getBannerData(): Observable<BannerBean>

    @GET("tree/json")
    abstract fun getTreeData(): Observable<TreeBean>

    @GET("project/tree/json")
    abstract fun getProjectCategoryData(): Observable<ProjectCategoryBean>

    @GET("article/list/{page}/json?")
    abstract fun getTreeArticleData(@Path("page") page: Int, @Query("cid") cid: Int): Observable<HomeBean>

    @GET("project/list/{page}/json")
    abstract fun getProjectDetailData(@Path("page") page: Int, @Query("cid") cid: Int): Observable<ProjectDetailBean>

    @GET("/hotkey/json")
    abstract fun getHotKeyData(): Observable<HotKeyBean>

    @POST("article/query/{page}/json")
    @FormUrlEncoded
    abstract fun getSearchData(@Path("page") page: Int, @Field("k") keyWord: String): Observable<SearchResultBean>

    @POST("user/login")
    @FormUrlEncoded
    abstract fun login(@Field("username") username: String, @Field("password") password: String): Observable<RegisterAndLoginBean>

    @POST("user/register")
    @FormUrlEncoded
    abstract fun register(@Field("username") username: String, @Field("password") password: String, @Field("repassword") repassword: String): Observable<RegisterAndLoginBean>

    /**
     * 登出
     * @return
     */
    @GET("user/logout/json")
    fun logout(): Observable<WanAndroidBaseResponse>

    @GET("lg/collect/list/{page}/json")
    fun getCollected(@Path("page") page: Int): Observable<HomeBean>

    //我的收藏页面取消收藏
    @POST("lg/uncollect/{id}/json")
    @FormUrlEncoded
    fun cancelColleted(@Path("id") id: Int, @Field("originId") originId: Int): Observable<WanAndroidBaseResponse>

    @POST("lg/todo/listnotdo/{type}/json/{page}")
    fun getTodoList(@Path("type") type: Int, @Path("page") page: Int): Observable<TodoListBean>    //获取未完成代办数据列表

    @POST("lg/todo/listdone/{type}/json/{page}")
    fun getTodoDoneList(@Path("type") type: Int, @Path("page") page: Int): Observable<TodoListBean>    //获取已完成代办数据列表

    //新增一条Todo
    @POST("lg/todo/add/json")
    @FormUrlEncoded
    fun addTodo(@Field("title") title: String,
                         @Field("content") content: String, @Field("date") date: String, @Field("type") type: Int): Observable<WanAndroidBaseResponse>

    //更新一条Todo内容
    @POST("lg/todo/update/{id}/json")
    @FormUrlEncoded
    fun updateTodo(@Path("id") id: Int, @Field("title") title: String, @Field("content") content: String,
                            @Field("date") date: String, @Field("status") status: Int, @Field("type") type: Int): Observable<WanAndroidBaseResponse>

    //删除一条Todo内容
    @POST("lg/todo/delete/{id}/json")
    fun deleteTodo(@Path("id") id: Int): Observable<WanAndroidBaseResponse>

    @POST("lg/collect/{id}/json")
    fun collectArticle(@Path("id") id: Int): Observable<WanAndroidBaseResponse>   //收藏文章

    @POST("lg/uncollect_originId/{id}/json")
    fun cancelColleted(@Path("id") id: Int): Observable<WanAndroidBaseResponse>

    @GET("wxarticle/chapters/json")
    fun getWXNumbersData(): Observable<WXNumbersBean>

    @GET("wxarticle/list/{id}/{page}/json")
    fun getWXArticleData(@Path("id") id: Int, @Path("page") page: Int): Observable<HomeBean>
}