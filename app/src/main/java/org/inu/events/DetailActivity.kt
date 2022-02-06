package org.inu.events

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.inu.events.databinding.ActivityDetailBinding
import org.inu.events.objects.IntentMessage.EVENT_ID
import org.inu.events.objects.IntentMessage.HOME_BOARD_INFO
import org.inu.events.objects.IntentMessage.POST_EDIT_INFO
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

    private fun extractEventIdAndLoad() {
        val extras = intent.extras ?: return
        if (intent.hasExtra(HOME_BOARD_INFO)) {
            val id = extras.getInt(HOME_BOARD_INFO)
            Log.d("tag", "게시글의 id는 $id")
            viewModel.load(id)
        }
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
                it ?: return@observe
                val intent = Intent(this, CommentActivity::class.java).apply {
                    putExtra(
                        EVENT_ID,
                        it
                    )
                }
                startActivity(intent)
            }
        )
    }

    private fun setupToolbar() {
        binding.detailToolbar.toolbarImageView.setOnClickListener { finish() }
        //todo - 툴바메뉴는 자신이 작성한 글일 경우에만 노출돼야함
        if(LoginGoogle(this).isLogin()){
            if(viewModel.isMyWriting()){
                setSupportActionBar(binding.detailToolbar.toolbarRegister)
                supportActionBar?.setDisplayShowTitleEnabled(false)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.event_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.fixToolbarMenu -> {
                Log.d("tag", "fixToolbarMenu menu clicked!")
                Intent(this, RegisterEventsActivity::class.java).apply {
                    putExtra(POST_EDIT_INFO, viewModel.eventIndex)
                }.run { binding.root.context.startActivity(this) }
                true
            }
            R.id.deleteToolbarMenu -> {
                Log.d("tag", "deleteToolbarMenu menu clicked!")
//                viewModel.deleteWriting()
//                Intent(this,MainActivity::class.java).
//                run{binding.root.context.startActivity(this)}
                true
            }
            R.id.signOutToolbarMenu->{
                LoginGoogle(this).signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu!!.findItem(R.id.signOutToolbarMenu).isEnabled = LoginGoogle(this).isLogin()
        return super.onPrepareOptionsMenu(menu)
        }
    override fun onStart() {
        super.onStart()
        extractEventIdAndLoad()
    }
}