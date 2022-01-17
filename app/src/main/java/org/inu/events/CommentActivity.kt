package org.inu.events

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.inu.events.adapter.CommentAdapter
import org.inu.events.databinding.ActivityCommentBinding
import org.inu.events.viewmodel.CommentViewModel

class CommentActivity : AppCompatActivity() ,LoginDialog.LoginDialog{

    private lateinit var commentViewModel : CommentViewModel
    private lateinit var commentBinding: ActivityCommentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        commentViewModel = ViewModelProvider(this)[CommentViewModel::class.java]
        commentBinding = DataBindingUtil.setContentView(this,R.layout.activity_comment)
        commentBinding.commentViewModel = commentViewModel
        commentBinding.lifecycleOwner = this

        commentBinding.commentRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CommentAdapter(commentViewModel.commentList)
        }

        setupButtons()
    }

    private fun setupButtons(){
        commentViewModel.btnClickEvent.observe(
            this,
            {
                isLogin()
            }
        )
    }



    // dialog
    private fun isLogin(){      // 로그인 다이어로그 보여주기
        LoginDialog().show(this,{  onOk() } , { onCancel() })
    }

    override fun onOk() {   // 로그인 확인을 눌렀을 때
        Log.d(TAG, "onOk: ")
    }

    override fun onCancel() {   // 로그인 취소를 눌렀을 때
        Toast.makeText(this,"로그인을 하셔야 댓글 작성이 가능합니다",Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "LoginActivity"
    }
}