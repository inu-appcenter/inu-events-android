package org.inu.events

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class LoginActivity : AppCompatActivity() {

    private val googleLoginButton: AppCompatButton by lazy {
        findViewById(R.id.googleLoginButton)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()
    }

    private fun initViews() {
        googleLoginButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}