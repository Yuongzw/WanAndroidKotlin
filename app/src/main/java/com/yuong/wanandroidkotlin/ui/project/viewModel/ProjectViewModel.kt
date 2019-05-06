package com.yuong.wanandroidkotlin.ui.project.viewModel

import android.view.View
import com.yuong.wanandroidkotlin.base.BaseViewModel
import com.yuong.wanandroidkotlin.bean.ProjectCategoryBean
import com.yuong.wanandroidkotlin.databinding.FragmentProjectBinding
import com.yuong.wanandroidkotlin.net.BaseLoadListener
import com.yuong.wanandroidkotlin.ui.adapter.MyProjectVPAdapter
import com.yuong.wanandroidkotlin.ui.project.model.ProjectModel
import com.yuong.wanandroidkotlin.ui.project.model.ProjectModelImpl
import com.yuong.wanandroidkotlin.widget.EmptyLayout

class ProjectViewModel: BaseViewModel<FragmentProjectBinding>(), BaseLoadListener<Any> {

    private lateinit var projectModel: ProjectModel

    lateinit var adapter: MyProjectVPAdapter

    override fun initUi() {
        adapter = MyProjectVPAdapter(activity!!.supportFragmentManager)
        binding!!.vpProject.adapter = adapter
        binding!!.projectTabLayout.setupWithViewPager(binding!!.vpProject)
        binding!!.projectEmptyLayout.setOnLayoutClickListener(View.OnClickListener {
            binding!!.projectEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING)
            projectModel.loadProjectCategotyData(this)
        })
    }

    override fun initNet() {
        projectModel = ProjectModelImpl(activity!!.applicationContext)
        projectModel.loadProjectCategotyData(this)
    }

    override fun loadSuccess(t: Any) {
        if (t is ProjectCategoryBean) run {
            val categoryBean = t
            if (categoryBean.data != null && categoryBean.data!!.size > 0) {
                binding!!.projectEmptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT)
                binding!!.projectContent.visibility = View.VISIBLE
                adapter.setList(categoryBean.data!!)
            }
        }
    }

    override fun loadFailure(message: String) {
        binding!!.projectEmptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR)
    }

    override fun loadStart() {
    }

    override fun loadComplete() {
    }
}