package org.inu.events.ui.mypage

import androidx.activity.viewModels
import org.inu.events.R
import org.inu.events.base.BaseActivity
import org.inu.events.databinding.ActivityMyPageBinding

class MyPageActivity : BaseActivity<ActivityMyPageBinding>() {
    override val layoutResourceId = R.layout.activity_my_page
    private val viewModel: MyPageViewModel by viewModels()

    override fun dataBinding() {
        super.dataBinding()
        binding.viewModel = viewModel
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchData()
        binding.executePendingBindings()
    }

    override fun afterDataBinding() {
        binding.toolbar.setOnBackListener {
            finish()
        }

        viewModel.loginEvent.observe(this) {
            finish()
        }
    }
}