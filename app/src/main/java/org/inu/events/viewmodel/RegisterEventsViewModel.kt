package org.inu.events.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.data.model.Article
import org.inu.events.data.service.EventService
import org.inu.events.di.AppConfigs
import org.inu.events.objects.EventNumber.EVENT_START_GALLERY
import org.inu.events.objects.EventNumber.EVENT_START_MAIN_ACTIVITY
import org.inu.events.util.SingleLiveEvent
import java.text.SimpleDateFormat
import java.util.*

class RegisterEventsViewModel : ViewModel() {

    // todo - 수연 : selectedItemPosition부분 어떻게 수정할지
    val selectedItemPosition = MutableLiveData(0)
    val startDatePeriod = MutableLiveData("")
    val startTimePeriod = MutableLiveData("")
    val endDatePeriod = MutableLiveData("")
    val endTimePeriod = MutableLiveData("")
    val target = MutableLiveData("")
    val content = MutableLiveData("")
    val selectedImageUri = MutableLiveData<Uri>()
    val phase = MutableLiveData(1)

    //서버통신
    private val eventService: EventService = AppConfigs.eventService

    //현재 이벤트 인덱스 저장
    var eventIndex = MutableLiveData<Int>()
        set(value){
            Log.d("tag","eventIndex set $eventIndex")
            field = value
            field.value = value.value
            _detailDataList.value = loadDetailData()
            spinnerSelected()
        }

    //디테일엑티비티에서 자신의 글인 경우 수정 버튼을 눌렀을 때 기존의 글을 불러와서 데이터 저장하기 위함
    private val _detailDataList = MutableLiveData<Article>()
    val detailDataList : MutableLiveData<Article>
        get() = _detailDataList

    init{
        _detailDataList.value = loadDetailData()
    }

    val startHomeActivityClickEvent = SingleLiveEvent<Any>()
    val startGalleryClickEvent = SingleLiveEvent<Any>()
    val startDatePickerClickEvent = SingleLiveEvent<Any>()
    val startTimePickerClickEvent = SingleLiveEvent<Any>()
    val endDatePickerClickEvent = SingleLiveEvent<Any>()
    val endTimePickerClickEvent = SingleLiveEvent<Any>()


    private fun loadDetailData(): Article{
        return eventService.getEventDetail(eventIndex.value)
    }

    private fun spinnerSelected(){
        when(_detailDataList.value!!.category){
            "선택없음"->selectedItemPosition.value = 0
            "동아리"->selectedItemPosition.value = 1
            "소모임"->selectedItemPosition.value = 2
            "간식나눔"->selectedItemPosition.value = 3
            "대회 공모전"->selectedItemPosition.value = 4
            "인턴"->selectedItemPosition.value = 5
            "기타"->selectedItemPosition.value = 6
            else->selectedItemPosition.value = 0
        }
        Log.d("tag","category = ${detailDataList.value?.category}," +
                "position = ${selectedItemPosition.value}")

    }

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