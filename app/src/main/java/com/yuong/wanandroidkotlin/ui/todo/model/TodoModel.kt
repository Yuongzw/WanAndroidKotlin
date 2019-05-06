package com.yuong.wanandroidkotlin.ui.todo.model

import com.yuong.wanandroidkotlin.net.BaseLoadListener

interface TodoModel {
    /**
     * 获取为完成的代办事件
     * @param type 代办事件类型
     * @param page  页数
     * @param loadListener
     */
    fun getTodoList(type: Int, page: Int, loadListener: BaseLoadListener<Any>)

    /**
     * 获取已完成的代办事件
     * @param type 代办事件类型
     * @param page 页数
     * @param loadListener
     */
    fun getTodoDoneList(type: Int, page: Int, loadListener: BaseLoadListener<Any>)

    /**
     * 增加一个代办事件
     * @param title 事件标题
     * @param content 事件内容
     * @param date 日期
     * @param type  类型
     * @param loadListener
     */
    fun addTodo(title: String, content: String, date: String, type: Int, loadListener: BaseLoadListener<Any>)

    /**
     * 更新一个代办事件
     * @param id 事件id
     * @param title 事件标题
     * @param content 事件内容
     * @param date  日期
     * @param status    状态
     * @param type  类型
     * @param loadListener
     */
    fun updateTodo(id: Int, title: String, content: String, date: String, status: Int, type: Int, loadListener: BaseLoadListener<Any>)

    /**
     * 删除一个代办事件
     * @param id    事件id
     * @param loadListener
     */
    fun deleteTodo(id: Int, loadListener: BaseLoadListener<Any>)
}