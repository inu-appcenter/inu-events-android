package org.inu.events

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import org.inu.events.adapter.CategoryAdapter
import org.inu.events.base.BaseActivity
import org.inu.events.databinding.ActivityNotificationSettingBinding
import org.inu.events.viewmodel.NotificationSettingViewModel

class NotificationSettingActivity : BaseActivity<ActivityNotificationSettingBinding>() {
    override val layoutResourceId = R.layout.activity_notification_setting
    private val viewModel: NotificationSettingViewModel by viewModels()

    override fun afterDataBinding() {
        binding.viewModel = viewModel
        binding.categories.adapter = CategoryAdapter()
    }
}