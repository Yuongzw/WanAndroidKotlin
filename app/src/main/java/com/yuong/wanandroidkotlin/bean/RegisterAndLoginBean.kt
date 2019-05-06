package com.yuong.wanandroidkotlin.bean

/**
 * 用户注册和登录数据模型
 */
class RegisterAndLoginBean {

    /**
     * data : {"chapterTops":[],"collectIds":[],"email":"","icon":"","id":11857,"password":"234567","token":"","type":0,"username":"234567"}
     * errorCode : 0
     * errorMsg :
     */

    var data: DataBean? = null
    var errorCode: Int = 0
    var errorMsg: String? = null

    class DataBean {
        /**
         * chapterTops : []
         * collectIds : []
         * email :
         * icon :
         * id : 11857
         * password : 234567
         * token :
         * type : 0
         * username : 234567
         */

        var email: String? = null
        var icon: String? = null
        var id: Int = 0
        var password: String? = null
        var token: String? = null
        var type: Int = 0
        var username: String? = null
        var chapterTops: List<*>? = null
        var collectIds: List<*>? = null
    }
}
