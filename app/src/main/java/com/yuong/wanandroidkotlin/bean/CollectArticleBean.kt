package com.yuong.wanandroidkotlin.bean

/**
 * 收藏列表数据模型
 */
class CollectArticleBean {

    /**
     * data : {"curPage":1,"datas":[{"author":"浪淘沙xud","chapterId":60,"chapterName":"Android Studio相关","courseId":13,"desc":"","envelopePic":"","id":28700,"link":"https://www.jianshu.com/p/88e32ef66ef2","niceDate":"刚刚","origin":"","originId":3373,"publishTime":1540175722000,"title":"Android 技能图谱学习路线","userId":11857,"visible":0,"zan":0},{"author":"鸿洋","chapterId":408,"chapterName":"鸿洋","courseId":13,"desc":"","envelopePic":"","id":28699,"link":"http://mp.weixin.qq.com/s?__biz=MzAxMTI4MTkwNQ==&mid=2650820397&idx=1&sn=dd26d1dc56a31bff805afbbf65e15d3d&scene=38#wechat_redirect","niceDate":"刚刚","origin":"","originId":3733,"publishTime":1540175666000,"title":"Android Studio自定义模板  写页面竟然可以如此轻松","userId":11857,"visible":0,"zan":0}],"offset":0,"over":true,"pageCount":1,"size":20,"total":2}
     * errorCode : 0
     * errorMsg :
     */

    var data: DataBean? = null
    var errorCode: Int = 0
    var errorMsg: String? = null

    class DataBean {
        /**
         * curPage : 1
         * datas : [{"author":"浪淘沙xud","chapterId":60,"chapterName":"Android Studio相关","courseId":13,"desc":"","envelopePic":"","id":28700,"link":"https://www.jianshu.com/p/88e32ef66ef2","niceDate":"刚刚","origin":"","originId":3373,"publishTime":1540175722000,"title":"Android 技能图谱学习路线","userId":11857,"visible":0,"zan":0},{"author":"鸿洋","chapterId":408,"chapterName":"鸿洋","courseId":13,"desc":"","envelopePic":"","id":28699,"link":"http://mp.weixin.qq.com/s?__biz=MzAxMTI4MTkwNQ==&mid=2650820397&idx=1&sn=dd26d1dc56a31bff805afbbf65e15d3d&scene=38#wechat_redirect","niceDate":"刚刚","origin":"","originId":3733,"publishTime":1540175666000,"title":"Android Studio自定义模板  写页面竟然可以如此轻松","userId":11857,"visible":0,"zan":0}]
         * offset : 0
         * over : true
         * pageCount : 1
         * size : 20
         * total : 2
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
             * author : 浪淘沙xud
             * chapterId : 60
             * chapterName : Android Studio相关
             * courseId : 13
             * desc :
             * envelopePic :
             * id : 28700
             * link : https://www.jianshu.com/p/88e32ef66ef2
             * niceDate : 刚刚
             * origin :
             * originId : 3373
             * publishTime : 1540175722000
             * title : Android 技能图谱学习路线
             * userId : 11857
             * visible : 0
             * zan : 0
             */

            var author: String? = null
            var chapterId: Int = 0
            var chapterName: String? = null
            var courseId: Int = 0
            var desc: String? = null
            var envelopePic: String? = null
            var id: Int = 0
            var link: String? = null
            var niceDate: String? = null
            var origin: String? = null
            var originId: Int = 0
            var publishTime: Long = 0
            var title: String? = null
            var userId: Int = 0
            var visible: Int = 0
            var zan: Int = 0
        }
    }
}
