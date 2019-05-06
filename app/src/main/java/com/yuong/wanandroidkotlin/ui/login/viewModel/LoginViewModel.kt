package com.yuong.wanandroidkotlin.ui.login.viewModel

import android.app.Activity
import android.content.DialogInterface
import android.databinding.ObservableField
import android.view.KeyEvent
import android.view.View
import com.yuong.wanandroidkotlin.MyConstans
import com.yuong.wanandroidkotlin.R
import com.yuong.wanandroidkotlin.base.BaseViewModel
import com.yuong.wanandroidkotlin.bean.RegisterAndLoginBean
import com.yuong.wanandroidkotlin.databinding.ActivityLoginBinding
import com.yuong.wanandroidkotlin.net.BaseLoadListener
import com.yuong.wanandroidkotlin.ui.login.model.LoginModel
import com.yuong.wanandroidkotlin.ui.login.model.LoginModelImpl
import com.yuong.wanandroidkotlin.util.SharedPreferenceUtil
import com.yuong.wanandroidkotlin.util.ToastUtils
import com.yuong.wanandroidkotlin.widget.MyProgressDialog

class LoginViewModel: BaseViewModel<ActivityLoginBinding>(), BaseLoadListener<Any> {

    private lateinit var model: LoginModel

    private lateinit var progressDialog: MyProgressDialog

    //用户名
    var username = ObservableField<String>("")
    //密码
    var password = ObservableField<String>("")
    //确认密码
    var rePassword = ObservableField<String>("")
    //是否记住密码
    var isRremember = ObservableField<Boolean>(false)

    var show = ObservableField<Boolean>(false)

    var btnText = ObservableField("登录")
    var text = ObservableField("去注册 >")

    private var isLogin: Boolean = false

    override fun initUi() {
        isRremember.set(SharedPreferenceUtil.getData(activity!!.applicationContext, "isRremember", false) as Boolean)
        if (isRremember.get()!!) {
            username.set(SharedPreferenceUtil.getData(activity!!.applicationContext, "username", "") as String)
            password.set(SharedPreferenceUtil.getData(activity!!.applicationContext, "password", "") as String)
        }
        initListener()
        initLoadingDialog()
    }

    private fun initListener() {
        binding!!.tvLoginRegister.setOnClickListener { view ->
            show.set(!show.get()!!)
            if (show.get()!!){
                btnText.set("注册")
                text.set("去登录 >")
            } else {
                btnText.set("登录")
                text.set("去注册 >")
            }
        }
        binding!!.loginBtn.setOnClickListener { view ->
            if (username.get()!!.length <= 0) {
                ToastUtils.showShort("请输入用户名！")
                return@setOnClickListener
            }
            if (password.get()!!.length <= 0) {
                ToastUtils.showShort("请输入密码！")
                return@setOnClickListener
            }
            if (btnText.get() == "登录") {
                model.login(username.get()!!, password.get()!!, this@LoginViewModel)
                showLoadingDialog("正在登录...")
            } else {
                if (rePassword.get()!!.length <= 0) {
                    ToastUtils.showShort("请输入确认密码！")
                    return@setOnClickListener
                }
                if (rePassword.get() != password.get()) {
                    ToastUtils.showShort("两次输入的密码不一致，请重新输入！")
                    return@setOnClickListener
                }
                model.register(username.get()!!, password.get()!!, rePassword.get()!!, this@LoginViewModel)
                showLoadingDialog("正在注册...")
            }
        }
    }

    fun initLoadingDialog() {
        progressDialog = MyProgressDialog(activity!!, R.style.MyProgressDialog)
        progressDialog.setOnKeyListener { dialogInterface, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_BACK){
                if (isLogin && progressDialog.isShowing) {
                    model.cancelLogin()
                    progressDialog.dismiss()
                    ToastUtils.showShort("取消登录！")
                    isLogin = false
                } else{

                }
            }
            false
        }
    }

    fun showLoadingDialog(text: String) {
        progressDialog.setText(text)
        progressDialog.show()
    }

    fun hideDialog() {
        progressDialog.dismiss()
    }

    override fun initNet() {
        model = LoginModelImpl(activity!!.applicationContext)
    }

    override fun loadSuccess(t: Any) {
        val registerAndLoginBean = t as RegisterAndLoginBean
        if (registerAndLoginBean.errorCode == -1) {
            ToastUtils.showShort(registerAndLoginBean.errorMsg)
            hideDialog()
            MyConstans.ISLOGIN = false
        } else {    //注册成功
            SharedPreferenceUtil.saveData(activity!!.applicationContext, "username", username.get()!!)
            SharedPreferenceUtil.saveData(activity!!.applicationContext, "password", password.get()!!)
            SharedPreferenceUtil.saveData(activity!!.applicationContext, "isRremember", isRremember.get()!!)
            MyConstans.ISLOGIN = true
            val collectIds = registerAndLoginBean.data!!.collectIds
            if (collectIds != null && collectIds!!.size > 0) {
                MyConstans.collectIds.clear()
                for (i in collectIds!!.indices) {
                    val collectId = collectIds!!.get(i) as Double
                    val id = collectId.toInt()
                    MyConstans.collectIds.add(id)
                }
            }
            hideDialog()
            activity!!.finish()
        }
    }

    override fun loadFailure(message: String) {
        hideDialog()
    }

    override fun loadStart() {
    }

    override fun loadComplete() {
    }
}