package org.inu.events

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.inu.events.adapter.CategoryAdapter
import org.inu.events.adapter.CategoryItemDecoration
import org.inu.events.base.BaseActivity
import org.inu.events.databinding.ActivityNotificationSettingBinding
import org.inu.events.viewmodel.NotificationSettingViewModel

class NotificationSettingActivity : BaseActivity<ActivityNotificationSettingBinding>() {
    override val layoutResourceId = R.layout.activity_notification_setting
    private val viewModel: NotificationSettingViewModel by viewModels()

    override fun dataBinding() {
        super.dataBinding()
        binding.viewModel = viewModel
        binding.categories.adapter = CategoryAdapter()
        binding.categories.addItemDecoration(CategoryItemDecoration(2, 8, true))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun afterDataBinding() {
        viewModel.loadSubscription()
        viewModel.finishEvent.observe(this) {
            finish()
        }

        binding.toolbar.setOnBackListener {
            finish()
        }
    }
}