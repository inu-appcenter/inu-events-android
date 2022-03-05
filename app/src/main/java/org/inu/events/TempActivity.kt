package org.inu.events

import androidx.activity.viewModels
import org.inu.events.adapter.RegisterStateAdapter
import org.inu.events.base.BaseActivity
import org.inu.events.databinding.ActivityTempBinding
import org.inu.events.viewmodel.TempViewModel

class TempActivity : BaseActivity<ActivityTempBinding>() {
    override val layoutResourceId = R.layout.activity_temp
    val viewModel: TempViewModel by viewModels()

    override fun dataBinding() {
        super.dataBinding()
        binding.viewModel = viewModel
    }

    override fun afterDataBinding() {
        binding.viewpager.adapter = RegisterStateAdapter(this)

        viewModel.onNextEvent.observe(this) {
            val position = binding.viewpager.currentItem
            binding.viewpager.currentItem = position + 1
        }

        viewModel.onPreviousEvent.observe(this) {
            val position = binding.viewpager.currentItem
            binding.viewpager.currentItem = position - 1
        }
    }
}