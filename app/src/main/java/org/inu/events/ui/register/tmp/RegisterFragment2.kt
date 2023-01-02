package org.inu.events.ui.register.tmp

import org.inu.events.R
import org.inu.events.base.BaseFragment
import org.inu.events.databinding.FragmentRegister2Binding

class RegisterFragment2(val registerViewModel: TempRegisterViewModel) : BaseFragment<FragmentRegister2Binding>() {
    override val layoutResourceId = R.layout.fragment_register2

    override fun dataBinding() {
        super.dataBinding()
        binding.viewModel = registerViewModel
    }
}