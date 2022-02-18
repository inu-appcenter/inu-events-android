package org.inu.events

import android.text.Editable
import android.text.TextWatcher
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

        binding.nicknameUpdate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.inputText.value = s.toString()
                viewModel.validateNickname()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}