package org.inu.events.ui.mypage.store

import androidx.activity.viewModels
import org.inu.events.R
import org.inu.events.ui.adapter.LikeAdapter
import org.inu.events.base.BaseActivity
import org.inu.events.databinding.ActivityLikeBinding

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
        binding.toolbar.setOnBackListener {
            finish()
        }
    }
}