package org.inu.events.ui.register.tmp

import org.inu.events.R
import org.inu.events.base.BaseFragment
import org.inu.events.databinding.FragmentRegister1Binding

class RegisterFragment1(val registerViewModel: TempRegisterViewModel) : BaseFragment<FragmentRegister1Binding>() {
    override val layoutResourceId = R.layout.fragment_register1

    override fun dataBinding() {
        super.dataBinding()
        binding.viewModel = registerViewModel
    }
}