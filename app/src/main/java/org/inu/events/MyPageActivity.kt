package org.inu.events

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import org.inu.events.base.BaseActivity
import org.inu.events.databinding.ActivityMyPageBinding
import org.inu.events.viewmodel.MyPageViewModel

class MyPageActivity : BaseActivity<ActivityMyPageBinding>() {
    override val layoutResourceId = R.layout.activity_my_page
    private val viewModel: MyPageViewModel by viewModels()

    override fun dataBinding() {
        super.dataBinding()
        binding.viewModel = viewModel
    }
}