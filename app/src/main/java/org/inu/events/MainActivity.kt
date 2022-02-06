package org.inu.events

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import org.inu.events.adapter.HomeAdapter
import org.inu.events.databinding.ActivityMainBinding
import org.inu.events.login.LoginGoogle
import org.inu.events.objects.IntentMessage
import org.inu.events.viewmodel.HomeViewModel

class MainActivity : AppCompatActivity(), LoginDialog.LoginDialog {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var loginService: LoginGoogle

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_InuEvents) // 이것도 지울 수 있을 것 같아요.
        super.onCreate(savedInstanceState)
        initBinding()
        setupRecyclerView()
        setupButtons()
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.mainViewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setupRecyclerView() {
        binding.homeRecyclerView.apply {
            adapter = HomeAdapter(viewModel.homeDataList)   //데이터를 아답터에 전달
        }
    }

    private fun setupButtons() {
        loginService = LoginGoogle(this)
        viewModel.postClickEvent.observe(
            this
        ) {
            if(loginService.isLogin()){
                //startActivity(Intent(this, RegisterEventsActivity::class.java))
                Intent(this,RegisterEventsActivity::class.java).apply {
                    putExtra(IntentMessage.POST_EDIT_INFO,-1)
                }.run{binding.root.context.startActivity(this)}
            }
            else{
                isLogin()
            }
        }
    }

    private fun isLogin() {
        LoginDialog().show(this, { onOk() }, { onCancel() })
    }

    override fun onOk() {
        loginService.signIn(this)
    }

    override fun onCancel() {
        Toast.makeText(this, "로그인을 하셔야 게시글 작성이 가능합니다", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1000){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            loginService.handleSignInResult(task)

        }
    }
}