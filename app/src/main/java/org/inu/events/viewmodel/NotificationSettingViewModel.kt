package org.inu.events.viewmodel

import androidx.lifecycle.ViewModel
import org.inu.events.R
import org.inu.events.adapter.Category

class NotificationSettingViewModel : ViewModel() {
    val categoryList = listOf(
        Category("동아리/소모임", false, R.drawable.club),
        Category("학생회", false, R.drawable.student_council),
        Category("간식나눔", false, R.drawable.snack),
        Category("대회/공모전", false, R.drawable.competition),
        Category("스터디", false, R.drawable.study),
        Category("구인", false, R.drawable.recruit),
        Category("기타", false, R.drawable.etc)
    )
}