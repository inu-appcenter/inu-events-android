package org.inu.events.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.inu.events.common.extension.toast
import org.inu.events.data.model.entity.Event
import org.inu.events.databinding.ActivitySearchBinding
import org.inu.events.ui.adapter.SearchPagingAdapter
import org.inu.events.ui.detail.DetailActivity
import org.inu.events.ui.login.LoginActivity
import org.inu.events.ui.util.dialog.LoginDialog

class SearchActivity : AppCompatActivity() {

    private val vm: SearchViewModel by viewModels()
    private lateinit var binding: ActivitySearchBinding
    private val adapter = SearchPagingAdapter(
        onClickEvent = {
            onClickSearchedEvent(it)
        },
        onCLickLikeIcon = {
            onClickLikeIcon(it)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@SearchActivity
            vm = this@SearchActivity.vm
        }

        initRecyclerView()
        initBackButton()
        observeSearch()

        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        vm.search()
    }

    private fun initRecyclerView() {
        binding.rvEvent.adapter = adapter
    }

    private fun observeSearch() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.searchResult.collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    private fun initBackButton() {
        binding.tvBack.setOnClickListener {
            finish()
        }
    }

    private fun onClickSearchedEvent(event: Event) {
        startActivity(DetailActivity.callingIntent(this, event.id, event.wroteByMe))
    }

    private fun onClickLikeIcon(event: Event) {
        vm.bookMarkByToggle(event.likedByMe, event.id).run {
            if (this.not()) showDialog()
            else vm.search()
        }
    }

    private fun showDialog() {
        LoginDialog().show(
            context = this,
            onOk = { this.startActivity(Intent(this, LoginActivity::class.java)) },
            onCancel = { toast("로그인을 하셔야 북마크가 가능합니다") })
    }
}