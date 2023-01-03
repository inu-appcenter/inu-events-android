package org.inu.events.ui.mypage.store

import androidx.activity.viewModels
import org.inu.events.R
import org.inu.events.ui.adapter.NotificationEventAdapter
import org.inu.events.base.BaseActivity
import org.inu.events.databinding.ActivityNotificationBinding

class NotificationActivity : BaseActivity<ActivityNotificationBinding>() {
    override val layoutResourceId = R.layout.activity_notification
    private val viewModel: NotificationViewModel by viewModels()

    override fun dataBinding() {
        super.dataBinding()
        binding.viewModel = viewModel
    }

    override fun afterDataBinding() {
        binding.events.adapter = NotificationEventAdapter(viewModel)

        viewModel.loadNotificationEvents()
        binding.toolbar.setOnBackListener {
            finish()
        }
    }
}