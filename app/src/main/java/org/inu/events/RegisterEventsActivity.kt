package org.inu.events

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import org.inu.events.databinding.RegisterEventsBinding
import org.inu.events.viewmodel.RegisterEventsViewModel

class RegisterEventsActivity:AppCompatActivity() {
    // todo : 수연 - 이후 다른 파일로 빼기
    private val GALLERY = 1

    private lateinit var registerModel: RegisterEventsViewModel
    private lateinit var binding: RegisterEventsBinding
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.register_events)
        registerModel = RegisterEventsViewModel()
        binding.registerViewModel = registerModel
        binding.lifecycleOwner = this

        //    todo : 수연 - 이후 설정 창으로 가도록 수정
        registerModel.viewEvent.observe(this, {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    RegisterEventsViewModel.EVENT_START_MY_ACTIVITY -> {
                        Intent(this, MainActivity::class.java).apply{
                            startActivity(this)
                        }
                    }
                }
            }
        })
    }








}
