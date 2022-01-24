package org.inu.events

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import org.inu.events.adapter.CommentAdapter
import org.inu.events.databinding.ActivityCommentBinding
import org.inu.events.login.LoginGoogle
import org.inu.events.viewmodel.CommentViewModel

class CommentActivity : AppCompatActivity(), LoginDialog.LoginDialog {
    private val commentViewModel: CommentViewModel by viewModels()
    private lateinit var commentBinding: ActivityCommentBinding
    private lateinit var loginService : LoginGoogle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initBinding()
        setupButtons()
        setupRecyclerView()
        setupToolbar()
    }

    private fun setupButtons() {
        commentViewModel.btnClickEvent.observe(
            this,
            {
                isLogin()
            }
        )
    }

    private fun initBinding() {
        commentBinding = DataBindingUtil.setContentView(this, R.layout.activity_comment)
        commentBinding.commentViewModel = commentViewModel
        commentBinding.lifecycleOwner = this
    }

    private fun setupRecyclerView() {
        commentBinding.commentRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CommentAdapter(commentViewModel.commentList)
        }
    }

    private fun setupToolbar() {
        commentBinding.commentToolbar.toolbarImageView.setOnClickListener { finish() }
    }

    // dialog
    private fun isLogin() {      // 로그인 다이어로그 보여주기
        LoginDialog().show(this, { onOk() }, { onCancel() })
    }

    // 로그인 확인을 눌렀을 때
    override fun onOk() {
        loginService = LoginGoogle(this)
        loginService.signIn(this)
    }

    // 로그인 취소를 눌렀을 때
    override fun onCancel() {
        Toast.makeText(this, "로그인을 하셔야 댓글 작성이 가능합니다", Toast.LENGTH_SHORT).show()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1000){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            loginService.handleSignInResult(task)

        }
    }
    companion object {
        const val TAG = "LoginActivity"
    }
}