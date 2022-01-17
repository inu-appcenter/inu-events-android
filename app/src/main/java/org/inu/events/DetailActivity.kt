package org.inu.events

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.inu.events.databinding.ActivityDetailBinding
import org.inu.events.viewmodel.DetailViewModel

class DetailActivity : AppCompatActivity() {
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        setupButtons()
        setupToolbar()
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        binding.detailViewModel = viewModel
        binding.lifecycleOwner = this

    }

    private fun setupButtons() {
        viewModel.commentClickEvent.observe(
            this,
            {
                val intent = Intent(this, CommentActivity::class.java)
                startActivity(intent)
            }
        )
    }

    private fun setupToolbar(){
        binding.detailToolbar.toolbarRegister.inflateMenu(R.menu.event_toolbar_menu)
        binding.detailToolbar.toolbarImageView.setOnClickListener { finish() }
    }
}