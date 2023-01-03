package org.inu.events.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.inu.events.ui.register.tmp.RegisterFragment1
import org.inu.events.ui.register.tmp.RegisterFragment2
import org.inu.events.ui.register.tmp.RegisterFragment3
import org.inu.events.ui.register.tmp.TempRegisterViewModel
import java.lang.IllegalArgumentException

class RegisterStateAdapter(fa: FragmentActivity, val registerViewModel: TempRegisterViewModel) : FragmentStateAdapter(fa) {
    companion object {
        const val NUMBER_OF_PAGE = 3
    }

    override fun getItemCount() = NUMBER_OF_PAGE

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RegisterFragment1(registerViewModel)
            1 -> RegisterFragment2(registerViewModel)
            2 -> RegisterFragment3(registerViewModel)
            else -> throw IllegalArgumentException("존재하지 않는 페이지입니다.")
        }
    }
}