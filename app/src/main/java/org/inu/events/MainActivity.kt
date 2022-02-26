package org.inu.events

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.inu.events.adapter.HomeAdapter
import org.inu.events.common.extension.observe
import org.inu.events.common.extension.observeNonNull
import org.inu.events.common.extension.toast
import org.inu.events.databinding.ActivityMainBinding
import org.inu.events.dialog.LoginDialog
import org.inu.events.googlelogin.GoogleLoginWrapper
import org.inu.events.service.LoginService
import org.inu.events.viewmodel.HomeViewModel
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), LoginDialog.LoginDialog {
    private val loginService: LoginService by inject()
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private val googleLogin = GoogleLoginWrapper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_InuEvents) // 이것도 지울 수 있을 것 같아요.
        super.onCreate(savedInstanceState)

        initBinding()
        setupButtons()
        setupRecyclerView()
        tryAutoLogin()
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainViewModel = viewModel
        binding.lifecycleOwner = this
        observeNonNull(loginService.isLoggedInLiveData) {
            viewModel.load()
        }
    }

    private fun setupRecyclerView() {
        val theAdapter = HomeAdapter()

        binding.homeRecyclerView.apply {
            adapter = theAdapter  //데이터를 아답터에 전달
        }

        observeNonNull(viewModel.homeDataList) {
            theAdapter.homeDataList = it
        }
    }

    private fun setupButtons() {
        observe(viewModel.postClickEvent) {
            if (loginService.isLoggedIn) {
                startActivity(RegisterEventsActivity.callingIntent(this))
            } else {
                askUserForLogin()
            }
        }
    }

    private fun askUserForLogin() {
        LoginDialog().show(this, ::onOk, ::onCancel)
    }

    override fun onOk() {
        googleLogin.signIn {
            toast("구글 로그인 성공. 액세스 토큰: $it")
            loginService.login(it)
        }
    }

    override fun onCancel() {
        toast("로그인을 하셔야 게시글 작성이 가능합니다")
    }

    private fun tryAutoLogin() {
        if (loginService.isAutoLoginPossible()) {
            loginService.tryAutoLogin()
        } else {
            viewModel.load()
        }
    }
}