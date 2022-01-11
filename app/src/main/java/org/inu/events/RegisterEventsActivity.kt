package org.inu.events

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.inu.events.databinding.RegisterEventsBinding

class RegisterEventsActivity:AppCompatActivity() {
    private lateinit var registerModel: RegisterEventsViewModel
    private lateinit var binding: RegisterEventsBinding
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.register_events)
        registerModel = RegisterEventsViewModel()
        binding.registerViewModel = registerModel
        binding.lifecycleOwner = this
    }
}
