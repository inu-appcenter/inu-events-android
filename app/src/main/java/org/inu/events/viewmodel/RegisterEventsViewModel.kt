package org.inu.events.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.util.SingleLiveEvent
import java.text.SimpleDateFormat
import java.util.*

//ViewModel()
class RegisterEventsViewModel : ViewModel() {

    val title = MutableLiveData("")
    val organization = MutableLiveData("")
    val selectedItemPosition = MutableLiveData(0)
    val startDatePeriod = MutableLiveData("")
    val startTimePeriod = MutableLiveData("")
    val endDatePeriod = MutableLiveData("")
    val endTimePeriod = MutableLiveData("")
    val target = MutableLiveData("")
    val content = MutableLiveData("")
    val imageResId = MutableLiveData(1)
    val phase = MutableLiveData(1)

    val startHomeActivityClickEvent = SingleLiveEvent<Any>()
    val startGalleryClickEvent = SingleLiveEvent<Any>()
    val startDatePickerClickEvent = SingleLiveEvent<Any>()
    val startTimePickerClickEvent = SingleLiveEvent<Any>()
    val endDatePickerClickEvent = SingleLiveEvent<Any>()
    val endTimePickerClickEvent = SingleLiveEvent<Any>()

    val selectedImageUri = MutableLiveData<Uri>()

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
        // TODO 이미지 업로드 후 uuid 받고 dto 채워서 포스트
    }

    fun onImageSelected(uri: Uri) {
        selectedImageUri.value = uri
    }

    fun onStartDateClick() {
        startDatePickerClickEvent.call()
    }

    fun onStartTimeClick() {
        startTimePickerClickEvent.call()
    }

    fun onEndDateClick() {
        endDatePickerClickEvent.call()
    }

    fun onEndTimeClick() {
        endTimePickerClickEvent.call()
    }

    fun setStartDate(date: Date) {
        startDatePeriod.value = formatDate(date)
    }

    fun setStartTime(date: Date) {
        startTimePeriod.value = formatTime(date)
    }

    fun setEndDate(date: Date) {
        endDatePeriod.value = formatDate(date)
    }

    fun setEndTime(date: Date) {
        endTimePeriod.value = formatTime(date)
    }

    private fun formatDate(date: Date) = SimpleDateFormat("yyyy.MM.dd", Locale("ko", "KR"))
        .format(date)
        .toString()

    private fun formatTime(date: Date) = SimpleDateFormat("h:mm a", Locale("en", "US"))
        .format(date)
        .toString()
}