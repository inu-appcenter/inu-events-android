package org.inu.events

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import org.inu.events.databinding.ActivityLoginBinding
import org.inu.events.googlelogin.GoogleLoginWrapper
import org.inu.events.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleLogin: GoogleLoginWrapper

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
        googleLogin = GoogleLoginWrapper(this)
        viewModel.loginClickEvent.observe(
            this
        ) {
            googleLogin.signIn(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != 1000) {
            return
        }

        googleLogin.handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data)) {
            Toast.makeText(this, "구글 로그인 성공. 액세스 토큰: $it", Toast.LENGTH_SHORT).show()

            login(accessToken = it)
            finish()
        }
    }

    private fun login(accessToken: String) {
        // TODO 액세스 토큰 들고 서버에 로그인 요청
    }
}