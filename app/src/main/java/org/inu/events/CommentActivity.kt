package org.inu.events

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import org.inu.events.adapter.CommentAdapter
import org.inu.events.common.extension.getIntExtra
import org.inu.events.databinding.ActivityCommentBinding
import org.inu.events.googlelogin.GoogleLoginWrapper
import org.inu.events.objects.IntentMessage
import org.inu.events.service.LoginService
import org.inu.events.viewmodel.CommentViewModel
import org.koin.android.ext.android.inject

class CommentActivity : AppCompatActivity(), LoginDialog.LoginDialog {
    private val commentViewModel: CommentViewModel by viewModels()
    private val loginService: LoginService by inject()

    private lateinit var commentBinding: ActivityCommentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        setupButtons()
        setupRecyclerView()
        setupToolbar()
    }

    private fun setupButtons() {
        commentViewModel.btnClickEvent.observe(
            this
        ) {
            if (loginService.isLoggedIn) {
                Toast.makeText(this, "로그인 되어있슴다, 서버로 댓글을 보내자 이제", Toast.LENGTH_SHORT).show()
                commentViewModel.postComment()
            } else {
                showDialog()
            }
        }
    }

    private fun initBinding() {
        commentBinding = DataBindingUtil.setContentView(this, R.layout.activity_comment)
        commentBinding.commentViewModel = commentViewModel
        commentBinding.lifecycleOwner = this
    }

    private fun setupRecyclerView() {
        val theAdapter = CommentAdapter()

        commentBinding.commentRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = theAdapter
        }

        commentViewModel.commentList.observe(this) {
            it ?: return@observe
            theAdapter.commentList = it
        }
    }

    private fun setupToolbar() {
        commentBinding.commentToolbar.toolbarImageView.setOnClickListener { finish() }
    }

    // dialog
    private fun showDialog() {      // 로그인 다이어로그 보여주기
        LoginDialog().show(this, { onOk() }, { onCancel() })
    }

    // 로그인 확인을 눌렀을 때
    override fun onOk() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    // 로그인 취소를 눌렀을 때
    override fun onCancel() {
        Toast.makeText(this, "로그인을 하셔야 댓글 작성이 가능합니다", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        extractEventIdAndLoad()
    }

    private fun extractEventIdAndLoad() {
        val id = getIntExtra(IntentMessage.EVENT_ID) ?: return

        commentViewModel.load(id)
    }
}