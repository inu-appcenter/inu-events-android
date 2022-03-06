package org.inu.events.viewmodel

import androidx.lifecycle.MutableLiveData

class Register2ViewModel {
    val sameCheckBoxBoolean = MutableLiveData(false)
    val hostCheckBoxBoolean = MutableLiveData(false)
    val contactNumberCheckBoxBoolean = MutableLiveData(false)
    val locationCheckBoxBoolean = MutableLiveData(false)
}