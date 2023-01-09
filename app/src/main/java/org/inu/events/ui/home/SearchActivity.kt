package org.inu.events.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.inu.events.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private val vm: SearchViewModel by viewModels()
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater).apply {
            vm = this@SearchActivity.vm
        }
    }
}