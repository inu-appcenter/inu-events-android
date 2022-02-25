package org.inu.events.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.inu.events.data.model.entity.Event
import org.inu.events.data.repository.NotificationRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NotificationViewModel : ViewModel(), KoinComponent {
    private val notificationRepository: NotificationRepository by inject()

    val notificationList = MutableLiveData<List<Event>>()

    fun loadNotificationEvents() {
        CoroutineScope(Dispatchers.Main).launch {
            val eventsDeferred = async(Dispatchers.IO) { notificationRepository.getNotifications() }
            val originList = eventsDeferred.await()
            val eventList = originList.map {
                it.event
            }
            notificationList.postValue(eventList)
        }
    }
}