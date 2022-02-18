package org.inu.events.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UpdateProfileViewModel : ViewModel() {
    val inputText = MutableLiveData<String>("맥도날드사고싶다")
}