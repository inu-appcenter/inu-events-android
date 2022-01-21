package org.inu.events.viewmodel

import android.util.Log
import androidx.databinding.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.objects.EventNumber.EVENT_START_GALLERY
import org.inu.events.objects.EventNumber.EVENT_START_MAIN_ACTIVITY
import org.inu.events.util.SingleLiveEvent

//ViewModel()
class RegisterEventsViewModel : ViewModel() {

    val title = MutableLiveData("")
    val organization = MutableLiveData("")

    // todo - 수연 : selectedItemPosition부분 어떻게 수정할지
    val selectedItemPosition = MutableLiveData(0)
    val start_date_period = MutableLiveData("")
    val start_time_period = MutableLiveData("")
    val end_date_period = MutableLiveData("")
    val end_time_period = MutableLiveData("")
    val target = MutableLiveData("")
    val content = MutableLiveData("")
    val imageResId = MutableLiveData(1)
    val phase = MutableLiveData(1)


//    var selectedItemPosition: Int
//        get() = registerEventData.selectedItemPosition
//        set(value){
//            registerEventData = registerEventData.copy(selectedItemPosition = value)
//        }

    val startHomeActivityClickEvent = SingleLiveEvent<Any>()
    val startGalleryClickEvent = SingleLiveEvent<Any>()
    val datePickerClickEvent = SingleLiveEvent<Any>()
    val timePickerClickEvent = SingleLiveEvent<Any>()

    fun onCancelClick() {
        startHomeActivityClickEvent.call()
    }

    fun onImageButtonClick() {
        startGalleryClickEvent.call()
    }

    fun onNextClick() {
        Log.i("BUTTON", "다음 버튼 클릭")
        phase.value = 2
    }

    fun onBeforeClick() {
        Log.i("BUTTON", "이전 버튼 클릭")
        phase.value = 1
    }

    fun onCompleteClick() {

    }

    fun onStartDateClick() {
        datePickerClickEvent.call()
    }

    fun onStartTimeClick() {
        timePickerClickEvent.call()
    }

    fun onEndDateClick() {
        datePickerClickEvent.call()
    }

    fun onEndTimeClick() {
        timePickerClickEvent.call()
    }

}