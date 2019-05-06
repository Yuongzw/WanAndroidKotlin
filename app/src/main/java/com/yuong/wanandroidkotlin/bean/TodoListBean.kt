package com.yuong.wanandroidkotlin.bean

/**
 * 代办列表数据模型
 */
class TodoListBean {

    /**
     * data : {"curPage":1,"datas":[{"completeDate":null,"completeDateStr":"","content":"","date":1540224000000,"dateStr":"2018-10-23","id":3472,"status":0,"title":"fsdggssf","type":1,"userId":11857}],"offset":0,"over":true,"pageCount":1,"size":20,"total":1}
     * errorCode : 0
     * errorMsg :
     */

    var data: DataBean? = null
    var errorCode: Int = 0
    var errorMsg: String? = null

    class DataBean {
        /**
         * curPage : 1
         * datas : [{"completeDate":null,"completeDateStr":"","content":"","date":1540224000000,"dateStr":"2018-10-23","id":3472,"status":0,"title":"fsdggssf","type":1,"userId":11857}]
         * offset : 0
         * over : true
         * pageCount : 1
         * size : 20
         * total : 1
         */

        var curPage: Int = 0
        var offset: Int = 0
        var isOver: Boolean = false
        var pageCount: Int = 0
        var size: Int = 0
        var total: Int = 0
        var datas: List<DatasBean>? = null

        class DatasBean {
            /**
             * completeDate : null
             * completeDateStr :
             * content :
             * date : 1540224000000
             * dateStr : 2018-10-23
             * id : 3472
             * status : 0
             * title : fsdggssf
             * type : 1
             * userId : 11857
             */

            var completeDate: Any? = null
            var completeDateStr: String? = null
            var content: String? = null
            var date: Long = 0
            var dateStr: String? = null
            var id: Int = 0
            var status: Int = 0
            var title: String? = null
            var type: Int = 0
            var userId: Int = 0
        }
    }
}
