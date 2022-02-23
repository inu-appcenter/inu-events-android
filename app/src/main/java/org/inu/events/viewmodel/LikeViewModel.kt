package org.inu.events.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.data.model.entity.Event

class LikeViewModel : ViewModel() {
    val eventList = MutableLiveData<List<Event>>(
        listOf(
            Event(
                1, 1, "123", "234", "324", "1241423", "", "sdf",
                "sdf", "sdf", "sdf", false
            )
        )
    )
}