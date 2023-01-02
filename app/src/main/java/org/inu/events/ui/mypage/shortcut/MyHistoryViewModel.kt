package org.inu.events.ui.mypage.shortcut

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import org.inu.events.ui.detail.DetailActivity
import org.inu.events.data.model.entity.Event
import org.inu.events.data.repository.MyRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MyHistoryViewModel : ViewModel(), KoinComponent {
    val title = MutableLiveData("내가 쓴 글")

    private val myRepository: MyRepository by inject()

    val eventList = MutableLiveData<List<Event>>()

    fun setTitle(isEvent: Boolean) {
        if(!isEvent) title.value = "댓글 단 글"
    }

    fun loadEvents(isEvent: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            val eventsDeferred =
                if(isEvent) async(Dispatchers.IO) { myRepository.getEvents() }
                else async(Dispatchers.IO) { myRepository.getComments() }

            val eventList = eventsDeferred.await()
            this@MyHistoryViewModel.eventList.postValue(eventList)
        }
    }

    fun onClickDetail(view: View, event: Event) {
        with(view.context) {
            startActivity(DetailActivity.callingIntent(this, event.id, event.wroteByMe))
        }
    }
}