package org.inu.events

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class PrivacyPolicyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)

        findViewById<UniLetterToolbar>(R.id.toolbar).setOnBackListener {
            finish()
        }
    }
}