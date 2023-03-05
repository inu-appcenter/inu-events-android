package org.inu.events.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.inu.events.data.model.entity.Event
import org.inu.events.databinding.ActivitySearchBinding
import org.inu.events.ui.adapter.SearchPagingAdapter
import org.inu.events.ui.detail.DetailActivity

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

    private fun onClickLikeIcon(event: Event): Boolean {
        return false
    }
}