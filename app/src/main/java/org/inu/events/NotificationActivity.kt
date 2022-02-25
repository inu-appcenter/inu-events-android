package org.inu.events

import androidx.activity.viewModels
import org.inu.events.base.BaseActivity
import org.inu.events.databinding.ActivityNotificationBinding
import org.inu.events.viewmodel.NotificationViewModel

class NotificationActivity : BaseActivity<ActivityNotificationBinding>() {
    override val layoutResourceId = R.layout.activity_notification
    private val viewModel: NotificationViewModel by viewModels()

    override fun dataBinding() {
        super.dataBinding()
        binding.viewModel = viewModel
    }
}