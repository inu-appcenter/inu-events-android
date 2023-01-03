package org.inu.events.ui.mypage.setting

import android.annotation.SuppressLint
import androidx.activity.viewModels
import org.inu.events.R
import org.inu.events.ui.adapter.CategoryAdapter
import org.inu.events.ui.adapter.CategoryItemDecoration
import org.inu.events.base.BaseActivity
import org.inu.events.databinding.ActivityNotificationSettingBinding

class NotificationSettingActivity : BaseActivity<ActivityNotificationSettingBinding>() {
    override val layoutResourceId = R.layout.activity_notification_setting
    private val viewModel: NotificationSettingViewModel by viewModels()

    override fun dataBinding() {
        super.dataBinding()
        binding.viewModel = viewModel
        binding.categories.adapter = CategoryAdapter()
        binding.categories.addItemDecoration(CategoryItemDecoration(2, 8))
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