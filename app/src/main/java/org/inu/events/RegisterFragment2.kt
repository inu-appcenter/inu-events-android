package org.inu.events

import org.inu.events.base.BaseFragment
import org.inu.events.databinding.FragmentRegister2Binding
import org.inu.events.viewmodel.TempViewModel

class RegisterFragment2(val viewModel: TempViewModel) : BaseFragment<FragmentRegister2Binding>() {
    override val layoutResourceId = R.layout.fragment_register2

    override fun dataBinding() {
        super.dataBinding()
        binding.viewModel = viewModel
    }
}