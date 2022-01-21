package org.inu.events

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.inu.events.databinding.ActivityDetailBinding
import org.inu.events.objects.IntentMessage.HOME_BOARD_INFO
import org.inu.events.objects.IntentMessage.POST_EDIT_INFO
import org.inu.events.viewmodel.DetailViewModel

class DetailActivity: AppCompatActivity() {
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        setupButtons()
        setupToolbar()
        getEventId()

    }

    private fun getEventId() {
        val extras = intent.extras?:null
        if(intent.hasExtra(HOME_BOARD_INFO)){
            var id:Int? = extras?.getInt(HOME_BOARD_INFO)
            Log.d("tag","게시글의 id는 $id")
            viewModel.eventIndex = MutableLiveData(id)
        }
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

    private fun setupToolbar(){
        binding.detailToolbar.toolbarImageView.setOnClickListener { finish() }
        //todo - 툴바메뉴는 자신이 작성한 글일 경우에만 노출돼야함
        if(viewModel.isMyWriting()){
            setSupportActionBar(binding.detailToolbar.toolbarRegister)
            supportActionBar?.setDisplayShowTitleEnabled(false)
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
                Log.d("tag","fixToolbarMenu menu clicked!")
                Intent(this,RegisterEventsActivity::class.java).apply {
                    putExtra(POST_EDIT_INFO,viewModel.eventIndex.value)
                }.run{binding.root.context.startActivity(this)}
                true
            }
            R.id.deleteToolbarMenu -> {
                Log.d("tag","deleteToolbarMenu menu clicked!")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}