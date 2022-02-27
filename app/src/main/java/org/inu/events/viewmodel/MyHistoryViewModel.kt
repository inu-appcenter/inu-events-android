package org.inu.events.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.inu.events.DetailActivity
import org.inu.events.data.model.entity.Event
import org.inu.events.data.repository.MyRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MyHistoryViewModel : ViewModel(), KoinComponent {
    val title = MutableLiveData("내가 쓴 글")

    private val myRepository: MyRepository by inject()

    val eventList = MutableLiveData<List<Event>>()

    fun loadEvents() {
        CoroutineScope(Dispatchers.Main).launch {
            val eventsDeferred = async(Dispatchers.IO) { myRepository.getEvents() }
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