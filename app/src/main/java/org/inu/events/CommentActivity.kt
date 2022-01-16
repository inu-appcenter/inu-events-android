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

        commentBinding.commentRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CommentAdapter(commentViewModel.commentList)
        }

        initViews()
    }


    private fun initViews(){
        commentBinding.commentEnrollButton.setOnClickListener {
            isLogin()
        }
    }

    private fun isLogin(){
        LoginDialog().show(this,{  onOk() } , { onCancel() })
    }

    override fun onOk() {
        Log.d(TAG, "onOk: ")
    }

    override fun onCancel() {
        Toast.makeText(this,"로그인을 하셔야 댓글 작성이 가능합니다",Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "LoginActivity"
    }
}