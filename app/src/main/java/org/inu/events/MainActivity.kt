package org.inu.events

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import org.inu.events.adapter.HomeAdapter
import org.inu.events.databinding.ActivityMainBinding
import org.inu.events.googlelogin.GoogleLoginWrapper
import org.inu.events.objects.IntentMessage
import org.inu.events.service.LoginService
import org.inu.events.viewmodel.HomeViewModel
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), LoginDialog.LoginDialog {
    private val loginService: LoginService by inject()
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private lateinit var googleLogin: GoogleLoginWrapper

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_InuEvents) // 이것도 지울 수 있을 것 같아요.
        super.onCreate(savedInstanceState)
        initBinding()
        setupRecyclerView()
        setupButtons()
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainViewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setupRecyclerView() {
        val theAdapter = HomeAdapter()

        binding.homeRecyclerView.apply {
            adapter = theAdapter  //데이터를 아답터에 전달
        }

        viewModel.homeDataList.observe(this) {
            it ?: return@observe

            theAdapter.homeDataList = it
        }
    }

    private fun setupButtons() {
        viewModel.postClickEvent.observe(this) {
            if (loginService.isLoggedIn) {
                //startActivity(Intent(this, RegisterEventsActivity::class.java))
                Intent(this, RegisterEventsActivity::class.java).apply {
                    putExtra(IntentMessage.POST_EDIT_INFO, -1)
                }.run { binding.root.context.startActivity(this) }
            } else {
                askUserForLogin()
            }
        }
    }

    private fun askUserForLogin() {
        LoginDialog().show(this, { onOk() }, { onCancel() })
    }

    override fun onOk() {
        googleLogin.signIn(this)
    }

    override fun onCancel() {
        Toast.makeText(this, "로그인을 하셔야 게시글 작성이 가능합니다", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            googleLogin.handleSignInResult(task) {
                Toast.makeText(this, "구글 로그인 성공. 액세스 토큰: $it", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        viewModel.load()
    }
}