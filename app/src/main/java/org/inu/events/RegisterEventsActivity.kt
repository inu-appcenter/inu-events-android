package org.inu.events

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.inu.events.databinding.RegisterEventsBinding
import org.inu.events.objects.EventNumber.EVENT_START_GALLERY
import org.inu.events.objects.EventNumber.EVENT_START_MAIN_ACTIVITY
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

        //    todo : 수연 - 이후 MAIN -> 설정 창으로 가도록 수정
        registerModel.viewEvent.observe(this, {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    EVENT_START_MAIN_ACTIVITY -> {
                        Intent(this, MainActivity::class.java).apply{
                            startActivity(this)
                        }
                    }
                    EVENT_START_GALLERY -> {
                        val intent = Intent()
                        intent.type = "image/*"
                        intent.action = Intent.ACTION_GET_CONTENT
                        startActivityForResult(intent,GALLERY)
                    }
                }
            }
        })
    }








}