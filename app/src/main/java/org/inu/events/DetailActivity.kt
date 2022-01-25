package org.inu.events

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.inu.events.databinding.ActivityDetailBinding
import org.inu.events.login.LoginGoogle
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
        binding = DataBindingUtil.setContentView(this,R.layout.activity_detail)
        binding.detailViewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setupButtons() {
        viewModel.commentClickEvent.observe(
            this,
            {
                val intent = Intent(this,CommentActivity::class.java)
                startActivity(intent)
            }
        )
    }


    private fun setupToolbar() {
        setSupportActionBar(binding.detailToolbar.toolbarRegister)
        binding.detailToolbar.toolbarImageView.setOnClickListener { finish() }
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.event_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.signOutToolbarMenu -> {
                LoginGoogle(this).signOut(this)

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu!!.findItem(R.id.signOutToolbarMenu).isEnabled = LoginGoogle(this).isLogin(this)
        return super.onPrepareOptionsMenu(menu)
    }
}