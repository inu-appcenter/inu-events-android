package org.inu.events

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import org.inu.events.adapter.HomeAdapter
import org.inu.events.databinding.ActivityMainBinding
import org.inu.events.viewmodel.HomeViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_InuEvents) // 이것도 지울 수 있을 것 같아요.
        super.onCreate(savedInstanceState)
        initBinding()
        setupRecyclerView()
        setupButtons()
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.mainViewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun setupRecyclerView() {
        binding.homeRecyclerView.apply {
            adapter = HomeAdapter(viewModel.homeDataList)   //데이터를 아답터에 전달
        }
    }

    private fun setupButtons() {
        viewModel.postClickEvent.observe(
            this,
            {
                val intent = Intent(this,RegisterEventsActivity::class.java)
                startActivity(intent)
            }
        )
    }
}