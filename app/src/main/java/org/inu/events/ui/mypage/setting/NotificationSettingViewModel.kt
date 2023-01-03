package org.inu.events.ui.mypage.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import org.inu.events.R
import org.inu.events.ui.adapter.Category
import org.inu.events.common.util.SingleLiveEvent
import org.inu.events.data.model.dto.Topics
import org.inu.events.data.repository.SubscriptionRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NotificationSettingViewModel : ViewModel(), KoinComponent {
    private val subscriptionRepository: SubscriptionRepository by inject()

    val finishEvent = SingleLiveEvent<Any>()

    val categoryList = MutableLiveData(listOf(
        Category("동아리/소모임", false, R.drawable.club),
        Category("학생회", false, R.drawable.student_council),
        Category("간식나눔", false, R.drawable.snack),
        Category("대회/공모전", false, R.drawable.competition),
        Category("스터디", false, R.drawable.study),
        Category("구인", false, R.drawable.recruit),
        Category("기타", false, R.drawable.etc)
    ))

    fun onClickFinish() {
        updateSubscription()
        finishEvent.call()
    }

    private fun updateSubscription() {
        CoroutineScope(Dispatchers.IO).launch {
            val list = categoryList.value!!
                .filter { it.isChecked }
                .map { it.name }
            val param = Topics(list)
            subscriptionRepository.putTopics(param)
            subscriptionRepository.putSubscribing(true)
        }
    }

    fun loadSubscription() {
        CoroutineScope(Dispatchers.Main).launch {
            val deferred = async(Dispatchers.IO) { subscriptionRepository.getTopics().topics }
            val topics = deferred.await()
            val resultCategoryList = categoryList.value!!.map {
                if( topics.contains(it.name) )
                    Category(
                        name = it.name,
                        isChecked = true,
                        imageSrc = it.imageSrc
                    )
                else
                    it
            }
            categoryList.postValue(resultCategoryList)
        }
    }
}