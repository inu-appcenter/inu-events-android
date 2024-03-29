package org.inu.events.ui.mypage.shortcut

import androidx.activity.viewModels
import org.inu.events.R
import org.inu.events.ui.adapter.BlockedAccountAdapter
import org.inu.events.base.BaseActivity
import org.inu.events.databinding.ActivityBlockedAccountBinding

class BlockedAccountActivity : BaseActivity<ActivityBlockedAccountBinding>() {
    override val layoutResourceId = R.layout.activity_blocked_account
    private val viewModel: BlockedAccountViewModel by viewModels()

    override fun dataBinding() {
        super.dataBinding()
        binding.viewModel = viewModel
    }

    override fun afterDataBinding() {
        binding.blockedAccountList.adapter = BlockedAccountAdapter(viewModel)

        binding.toolbar.setOnBackListener {
            finish()
        }

        viewModel.loadData()
    }
}