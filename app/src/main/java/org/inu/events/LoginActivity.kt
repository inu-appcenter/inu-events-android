package org.inu.events

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.inu.events.common.extension.observe
import org.inu.events.common.extension.toast
import org.inu.events.databinding.ActivityLoginBinding
import org.inu.events.googlelogin.GoogleLoginWrapper
import org.inu.events.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding

    private val googleLogin = GoogleLoginWrapper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initViews()
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.loginViewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun initViews() {
        observe(viewModel.loginClickEvent) {
            googleLogin.signIn {
                viewModel.login(accessToken = it)
            }
        }

        observe(viewModel.loggedIn) {
            if (it == true) {
                toast("로그인 성공\n유니레터에 오신 것을 환영합니다!")
                finish()
            }
        }
    }
}