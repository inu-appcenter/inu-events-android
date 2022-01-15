package org.inu.events

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.inu.events.adapter.CommentAdapter
import org.inu.events.databinding.ActivityCommentBinding
import org.inu.events.viewmodel.CommentViewModel

class CommentActivity : AppCompatActivity() {

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
    }
}