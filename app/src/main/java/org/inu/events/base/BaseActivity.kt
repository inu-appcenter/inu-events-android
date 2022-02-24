package org.inu.events.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity() {
    abstract val layoutResourceId: Int
    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResourceId)
        binding.lifecycleOwner = this
        dataBinding()
        afterDataBinding()
    }

    open fun dataBinding() {

    }

    open fun afterDataBinding() {

    }
}