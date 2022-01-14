package org.inu.events.viewmodel

import android.content.Intent
import android.util.Log
import androidx.databinding.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.Event
import org.inu.events.data.RegisterEventData

//ViewModel()
class RegisterEventsViewModel : RegisterEventBaseViewModel() {
    companion object {
        const val EVENT_START_MAIN_ACTIVITY = 22212
        const val EVENT_START_GALLERY = 22213
    }

    val title = MutableLiveData("")
    val organization = MutableLiveData("")

    // todo - 수연 : selectedItemPosition부분 어떻게 수정할지
    val selectedItemPosition = MutableLiveData(0)
    val period = MutableLiveData("")
    val target = MutableLiveData("")
    val content = MutableLiveData("")
    val imageResId = MutableLiveData(1)
    val phase = MutableLiveData(1)


//    var selectedItemPosition: Int
//        get() = registerEventData.selectedItemPosition
//        set(value){
//            registerEventData = registerEventData.copy(selectedItemPosition = value)
//        }

    fun onCancelClick() = viewEvent(EVENT_START_MAIN_ACTIVITY)

    fun onNextClick() {
        Log.i("BUTTON", "다음 버튼 클릭")
        //registerEventData = registerEventData.copy(phase = 2)
        phase.value = 2
    }

    fun onBeforeClick() {
        Log.i("BUTTON", "이전 버튼 클릭")
        //registerEventData = registerEventData.copy(phase = 1)
        phase.value = 1
    }

    fun onCompleteClick() {
    }

    fun onImageButtonClick() = viewEvent(EVENT_START_GALLERY)

}