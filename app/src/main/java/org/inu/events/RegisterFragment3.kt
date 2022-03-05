package org.inu.events

import org.inu.events.base.BaseFragment
import org.inu.events.databinding.FragmentRegister3Binding
import org.inu.events.viewmodel.TempRegisterViewModel

class RegisterFragment3(val registerViewModel: TempRegisterViewModel) : BaseFragment<FragmentRegister3Binding>() {
    override val layoutResourceId = R.layout.fragment_register3

    override fun dataBinding() {
        super.dataBinding()
        binding.viewModel = registerViewModel
    }
}