package org.inu.events

import org.inu.events.base.BaseFragment
import org.inu.events.databinding.FragmentRegister1Binding
import org.inu.events.viewmodel.TempViewModel

class RegisterFragment1(val viewModel: TempViewModel) : BaseFragment<FragmentRegister1Binding>() {
    override val layoutResourceId = R.layout.fragment_register1

    override fun dataBinding() {
        super.dataBinding()
        binding.viewModel = viewModel
    }
}