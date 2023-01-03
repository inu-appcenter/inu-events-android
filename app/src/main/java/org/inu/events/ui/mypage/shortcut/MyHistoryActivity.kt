package org.inu.events.ui.mypage.shortcut

import androidx.activity.viewModels
import org.inu.events.R
import org.inu.events.ui.adapter.HistoryAdapter
import org.inu.events.base.BaseActivity
import org.inu.events.databinding.ActivityMyHistoryBinding

class MyHistoryActivity : BaseActivity<ActivityMyHistoryBinding>() {
    override val layoutResourceId = R.layout.activity_my_history
    private val viewModel: MyHistoryViewModel by viewModels()

    override fun dataBinding() {
        super.dataBinding()
        binding.viewModel = viewModel
    }

    override fun afterDataBinding() {
        binding.events.adapter = HistoryAdapter(viewModel)

        val isEvent = intent.getBooleanExtra("isEvent", true)
        viewModel.setTitle(isEvent)
        viewModel.loadEvents(isEvent)
        binding.toolbar.setOnBackListener {
            finish()
        }
    }
}