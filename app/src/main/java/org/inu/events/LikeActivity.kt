package org.inu.events

import androidx.activity.viewModels
import org.inu.events.adapter.LikeAdapter
import org.inu.events.base.BaseActivity
import org.inu.events.databinding.ActivityLikeBinding
import org.inu.events.viewmodel.LikeViewModel

class LikeActivity : BaseActivity<ActivityLikeBinding>() {
    override val layoutResourceId = R.layout.activity_like
    private val viewModel: LikeViewModel by viewModels()

    override fun dataBinding() {
        super.dataBinding()
        binding.viewModel = viewModel
    }

    override fun afterDataBinding() {
        binding.events.adapter = LikeAdapter(viewModel)

        viewModel.loadLikes()
        viewModel.onClickBackEvent.observe(this) {
            finish()
        }
    }
}