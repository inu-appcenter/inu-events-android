package org.inu.events

import androidx.activity.viewModels
import org.inu.events.adapter.HistoryAdapter
import org.inu.events.adapter.LikeAdapter
import org.inu.events.base.BaseActivity
import org.inu.events.databinding.ActivityMyHistoryBinding
import org.inu.events.viewmodel.MyHistoryViewModel

class MyHistoryActivity : BaseActivity<ActivityMyHistoryBinding>() {
    override val layoutResourceId = R.layout.activity_my_history
    private val viewModel: MyHistoryViewModel by viewModels()

    override fun dataBinding() {
        super.dataBinding()
        binding.viewModel = viewModel
    }

    override fun afterDataBinding() {
        binding.events.adapter = HistoryAdapter(viewModel)

        viewModel.loadEvents()
        binding.toolbar.setOnBackListener {
            finish()
        }
    }
}