package org.inu.events

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity: AppCompatActivity() {

    private val commentTextView: TextView by lazy{
        findViewById(R.id.commentTextView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initViews()
    }

    private fun initViews() {
        commentTextView.setOnClickListener {
            startActivity(Intent(this,CommentActivity::class.java))
        }
    }
}