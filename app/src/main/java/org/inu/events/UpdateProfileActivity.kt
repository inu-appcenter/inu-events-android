package org.inu.events

import androidx.activity.viewModels
import org.inu.events.base.BaseActivity
import org.inu.events.databinding.ActivityUpdateProfileBinding
import org.inu.events.viewmodel.UpdateProfileViewModel

class UpdateProfileActivity : BaseActivity<ActivityUpdateProfileBinding>() {
    override val layoutResourceId = R.layout.activity_update_profile
    private val viewModel: UpdateProfileViewModel by viewModels()

    override fun dataBinding() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }
}