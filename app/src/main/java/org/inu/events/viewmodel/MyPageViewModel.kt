package org.inu.events.viewmodel

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import org.inu.events.PrivacyPolicyActivity
import org.inu.events.UpdateProfileActivity

class MyPageViewModel : ViewModel() {
    fun onClickProfileUpdate(view: View) {
        val intent = Intent(view.context, UpdateProfileActivity::class.java)
        view.context.startActivity(intent)
    }

    fun onClickPrivacyPolicy(view: View) {
        val intent = Intent(view.context, PrivacyPolicyActivity::class.java)
        view.context.startActivity(intent)
    }
}