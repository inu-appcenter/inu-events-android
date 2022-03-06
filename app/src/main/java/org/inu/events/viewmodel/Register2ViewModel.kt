package org.inu.events.viewmodel

import androidx.lifecycle.MutableLiveData
import org.inu.events.data.model.entity.Event

class Register2ViewModel {
    val sameCheckBoxBoolean = MutableLiveData(false)
    val hostCheckBoxBoolean = MutableLiveData(false)
    val contactNumberCheckBoxBoolean = MutableLiveData(false)
    val locationCheckBoxBoolean = MutableLiveData(false)

    fun setCheckBoxState(event: Event?) {
        hostCheckBoxBoolean.value = (event?.host == null)
        locationCheckBoxBoolean.value = (event?.location == null)
        contactNumberCheckBoxBoolean.value = (event?.contact == null)
        sameCheckBoxBoolean.value = (event?.startAt == event?.endAt)
    }
}