package org.inu.events.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.data.model.Article
import org.inu.events.data.service.EventService
import org.inu.events.di.AppConfigs
import org.inu.events.util.SingleLiveEvent
import java.text.SimpleDateFormat
import java.util.*

class RegisterEventsViewModel : ViewModel() {

    // todo - 수연 : selectedItemPosition 부분 어떻게 수정할지
    val selectedItemPosition = MutableLiveData(0)
    val startDatePeriod = MutableLiveData("")
    val startTimePeriod = MutableLiveData("")
    val endDatePeriod = MutableLiveData("")
    val endTimePeriod = MutableLiveData("")
    val target = MutableLiveData("")
    val content = MutableLiveData("")
    val selectedImageUri = MutableLiveData<Uri>()
    val phase = MutableLiveData(1)

    //기존 글 수정 시 타임피커와 데이트피커 값을 불러오기 위한 정보 저장 변수
    private val cal = Calendar.getInstance()
    var datePickerValueStartYear:Int = cal.get(Calendar.YEAR)
    var datePickerValueStartMonth:Int = cal.get(Calendar.MONTH)
    var datePickerValueStartDay:Int = cal.get(Calendar.DATE)
    var datePickerValueEndYear:Int = cal.get(Calendar.YEAR)
    var datePickerValueEndMonth:Int = cal.get(Calendar.MONTH)
    var datePickerValueEndDay:Int = cal.get(Calendar.DATE)
    var timePickerValueStartTime:Int = cal.get(Calendar.HOUR_OF_DAY)
    var timePickerValueStartMinute:Int = cal.get(Calendar.MINUTE)
    var timePickerValueEndTime:Int = cal.get(Calendar.HOUR_OF_DAY)
    var timePickerValueEndMinute:Int = cal.get(Calendar.MINUTE)

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
            datePickerSelect()
            timePickerSelect()
        }


    //디테일엑티비티에서 자신의 글인 경우 수정 버튼을 눌렀을 때 기존의 글을 불러와서 데이터 저장하기 위함
    private val _detailDataList = MutableLiveData<Article>()
    val detailDataList : MutableLiveData<Article>
        get() = _detailDataList


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

    private fun dateFormat(year:String, month:String, day:String) = "%s.%s.%s".format(year,month,day)

    private fun timeFormat(hour:String, minute:String) =
                                "%s:%s %s".format(if(hour.toInt() > 12) (hour.toInt()-12).toString() else hour,
                                minute,
                                if(hour.toInt() > 12) "PM" else "AM")


    private fun datePickerSelect(){
        val startYear = _detailDataList.value!!.start_at.slice(IntRange(0,3))
        val startMonth = _detailDataList.value!!.start_at.slice(IntRange(5,6))
        val startDay = _detailDataList.value!!.start_at.slice(IntRange(8,9))
        val endYear = _detailDataList.value!!.end_at.slice(IntRange(0,3))
        val endMonth = _detailDataList.value!!.end_at.slice(IntRange(5,6))
        val endDay = _detailDataList.value!!.end_at.slice(IntRange(8,9))
        startDatePeriod.value = dateFormat(startYear,startMonth,startDay)
        endDatePeriod.value = dateFormat(endYear,endMonth,endDay)
        datePickerValueStartYear = startYear.toInt()
        datePickerValueStartMonth = startMonth.toInt()
        datePickerValueStartDay = startDay.toInt()
        datePickerValueEndYear = endYear.toInt()
        datePickerValueEndMonth = endMonth.toInt()
        datePickerValueEndDay = endDay.toInt()
    }

    private fun timePickerSelect(){
        val startHour = _detailDataList.value!!.start_at.slice(IntRange(11,12))
        val startMinute = _detailDataList.value!!.start_at.slice(IntRange(14,15))
        val endHour = _detailDataList.value!!.end_at.slice(IntRange(11,12))
        val endMinute = _detailDataList.value!!.end_at.slice(IntRange(14,15))
        startTimePeriod.value = timeFormat(startHour,startMinute)
        endTimePeriod.value = timeFormat(endHour, endMinute)
        timePickerValueStartTime = startHour.toInt()
        timePickerValueStartMinute = startMinute.toInt()
        timePickerValueEndTime = endHour.toInt()
        timePickerValueEndMinute= endMinute.toInt()
    }



}