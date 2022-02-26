package org.inu.events

import android.os.Bundle
import android.widget.Toast
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
                toast("구글 로그인 성공. 액세스 토큰: $it")
                viewModel.login(accessToken = it)
                finish()
            }
        }
    }
}