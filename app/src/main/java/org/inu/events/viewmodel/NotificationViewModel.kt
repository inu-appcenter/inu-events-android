package org.inu.events.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.inu.events.BackButtonListener
import org.inu.events.DetailActivity
import org.inu.events.dialog.TwoButtonDialog
import org.inu.events.common.util.SingleLiveEvent
import org.inu.events.data.model.dto.NotificationParams
import org.inu.events.data.model.entity.Event
import org.inu.events.data.repository.NotificationRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NotificationViewModel : ViewModel(), KoinComponent {
    private val notificationRepository: NotificationRepository by inject()

    val notificationList = MutableLiveData<List<Event>>()
    val onClickBackEvent = SingleLiveEvent<Any>()

    val backButtonListener = object : BackButtonListener {
        override fun invoke(view: View) {
            onClickBackEvent.call()
        }
    }

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

    fun onClickBookmarkIcon(eventId: Int, view: View) {
        TwoButtonDialog("알림을 취소하시겠어요?") { deleteNotification(eventId) }.show(view.context)
    }

    private fun deleteNotification(eventId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            var notificationParams = NotificationParams(
                eventId = eventId,
                setFor = "start"
            )
            notificationRepository.deleteNotification(notificationParams)
            notificationParams = NotificationParams(
                eventId = eventId,
                setFor = "end"
            )
            notificationRepository.deleteNotification(notificationParams)

            loadNotificationEvents()
        }
    }

    fun onClickDetail(view: View, event: Event) {
        with(view.context) {
            startActivity(DetailActivity.callingIntent(this, event.id, event.wroteByMe))
        }
    }
}