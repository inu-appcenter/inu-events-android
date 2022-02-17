package org.inu.events.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.R
import org.inu.events.common.extension.getIntExtra
import org.inu.events.common.threading.execute
import org.inu.events.common.util.SingleLiveEvent
import org.inu.events.data.model.dto.AddEventParams
import org.inu.events.data.model.dto.UpdateEventParams
import org.inu.events.data.model.entity.Event
import org.inu.events.data.repository.EventRepository
import org.inu.events.objects.IntentMessage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.SimpleDateFormat
import java.util.*

class RegisterEventsViewModel : ViewModel(), KoinComponent {
    private val context: Context by inject()
    private val eventRepository: EventRepository by inject()

    val selectedItemPosition = MutableLiveData(0)
    val startDatePeriod = MutableLiveData("")
    val startTimePeriod = MutableLiveData("")
    val endDatePeriod = MutableLiveData("")
    val endTimePeriod = MutableLiveData("")
    val content = MutableLiveData("")
    val selectedImageUri = MutableLiveData<Uri>()
    val phase = MutableLiveData(0)
    val title = MutableLiveData("")
    val body = MutableLiveData("")
    val host = MutableLiveData("")
    val submissionUrl = MutableLiveData("")
    val imageUuid = MutableLiveData("")

    var btnIndex = 0
    //기존 글 수정 시 타임피커와 데이트피커 값을 불러오기 위한 정보 저장 변수
    private val cal = Calendar.getInstance()
    var datePickerValueStartYear = cal.get(Calendar.YEAR)
    var datePickerValueStartMonth = cal.get(Calendar.MONTH)
    var datePickerValueStartDay = cal.get(Calendar.DATE)
    var datePickerValueEndYear = cal.get(Calendar.YEAR)
    var datePickerValueEndMonth = cal.get(Calendar.MONTH)
    var datePickerValueEndDay = cal.get(Calendar.DATE)
    var timePickerValueStartTime = cal.get(Calendar.HOUR_OF_DAY)
    var timePickerValueStartMinute = cal.get(Calendar.MINUTE)
    var timePickerValueEndTime = cal.get(Calendar.HOUR_OF_DAY)
    var timePickerValueEndMinute = cal.get(Calendar.MINUTE)

    val startHomeActivityClickEvent = SingleLiveEvent<Any>()
    val startGalleryClickEvent = SingleLiveEvent<Any>()
    val startDatePickerClickEvent = SingleLiveEvent<Any>()
    val startTimePickerClickEvent = SingleLiveEvent<Any>()
    val endDatePickerClickEvent = SingleLiveEvent<Any>()
    val endTimePickerClickEvent = SingleLiveEvent<Any>()
    val finishEvent = SingleLiveEvent<Any>()

    var eventIndex = -1
        private set

    private val isItNew: Boolean
        get() = eventIndex == -1

    var currentEvent: Event? = null

    fun load(eventId: Int) {
        eventIndex = eventId

        if(!isItNew){
            loadCurrentEvent()
        }
    }

    private fun loadCurrentEvent() {
        execute {
            eventRepository.getEvent(eventIndex)
        }.then{
            currentEvent = it
            title.value = currentEvent?.title
            body.value = currentEvent?.body
            host.value = currentEvent?.host
            submissionUrl.value = currentEvent?.submissionUrl
            spinnerSelected()
            datePickerSelect()
            timePickerSelect()
        }.catch { }
    }

    //행사 수정 시 서버에서 받아온 카테고리 이름 문자열을 스피너 선택으로 바꿔주는 함수
    private fun spinnerSelected() {
        when (currentEvent?.category) {
            "선택없음" -> selectedItemPosition.value = 0
            "동아리" -> selectedItemPosition.value = 1
            "소모임" -> selectedItemPosition.value = 2
            "간식나눔" -> selectedItemPosition.value = 3
            "대회 공모전" -> selectedItemPosition.value = 4
            "인턴" -> selectedItemPosition.value = 5
            "기타" -> selectedItemPosition.value = 6
            else -> selectedItemPosition.value = 0
        }
    }

    //행사 수정한 값을 반영할 때 스피너 값에 따라서 문자열 선택하는 함수
    private fun spinnerToCategory(): String {
        val array = context.resources.getStringArray(R.array.classification)
        return array[selectedItemPosition.value!!]
    }

    private fun datePickerToStartAt(): String {
        return formatDateForServer(startDatePeriod.value!! + startTimePeriod.value!!)
    }

    private fun datePickerToEndAt(): String {
        return formatDateForServer(endDatePeriod.value!! + endTimePeriod.value!!)

    }

    private fun addEvent() {
        eventRepository.postEvent(
            AddEventParams(
                host = host.value!!,
                category = spinnerToCategory(),
                title = title.value!!,
                body = body.value!!,
                imageUuid = imageUuid.value!!,
                submissionUrl = submissionUrl.value!!,
                startAt = datePickerToStartAt(),
                endAt = datePickerToEndAt(),
            )
        )
    }

    private fun updateEvent() {
        eventRepository.updateEvent(
            currentEvent!!.id,
            UpdateEventParams(
                host = host.value!!,
                category = spinnerToCategory(),
                title = title.value!!,
                body = body.value!!,
                imageUuid = imageUuid.value!!,
                submissionUrl = submissionUrl.value!!,
                startAt = datePickerToStartAt(),
                endAt = datePickerToEndAt()
            )
        )
    }

    fun onCancelClick() {
        startHomeActivityClickEvent.call()
    }

    fun onImageButtonClick() {
        startGalleryClickEvent.call()
    }

    fun onNextClick() {
        Log.i("BUTTON", "다음 버튼 클릭")
        btnIndex++
        phase.value = btnIndex
    }

    fun onBeforeClick() {
        Log.i("BUTTON", "이전 버튼 클릭")
        btnIndex--
        phase.value = btnIndex
    }

    fun onCompleteClick() {
        if (isItNew) {
            addEvent()
        } else {
            updateEvent()
        }
        finishEvent.call()
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

    private fun formatTime(date: Date) = SimpleDateFormat("hh:mm a", Locale("en", "US"))
        .format(date)
        .toString()

    private fun formatDateForServer(date: String): String {
        Log.d("tag", "date = $date")
        val year = date.slice(IntRange(0, 3))
        val month = date.slice(IntRange(5, 6))
        val day = date.slice(IntRange(8, 9))
        val amPm = date.slice(IntRange(16, 17))
        val hour = date.slice(IntRange(10, 11))
        val hourStr = if (amPm == "PM") (hour.toInt() + 12) else hour
        val minute = date.slice(IntRange(13, 14))
        val second = "00"
        return "%s-%s-%sT%s:%s:%s".format(year, month, day, hourStr, minute, second)
    }

    private fun dateFormat(year: String, month: String, day: String) =
        "%s.%s.%s".format(year, month, day)

    private fun timeFormat(hour: String, minute: String) =
        "%s:%s %s".format(
            if (hour.toInt() > 12) "0" + (hour.toInt() - 12).toString() else hour,
            minute,
            if (hour.toInt() > 12) "PM" else "AM"
        )

    private fun datePickerSelect() {
        val startYear = currentEvent!!.startAt.slice(IntRange(0, 3))
        val startMonth = currentEvent!!.startAt.slice(IntRange(5, 6))
        val startDay = currentEvent!!.startAt.slice(IntRange(8, 9))
        val endYear = currentEvent!!.endAt.slice(IntRange(0, 3))
        val endMonth = currentEvent!!.endAt.slice(IntRange(5, 6))
        val endDay = currentEvent!!.endAt.slice(IntRange(8, 9))
        startDatePeriod.value = dateFormat(startYear, startMonth, startDay)
        endDatePeriod.value = dateFormat(endYear, endMonth, endDay)
        datePickerValueStartYear = startYear.toInt()
        datePickerValueStartMonth = startMonth.toInt()
        datePickerValueStartDay = startDay.toInt()
        datePickerValueEndYear = endYear.toInt()
        datePickerValueEndMonth = endMonth.toInt()
        datePickerValueEndDay = endDay.toInt()
    }

    private fun timePickerSelect() {
        val startHour = currentEvent!!.startAt.slice(IntRange(11, 12))
        val startMinute = currentEvent!!.startAt.slice(IntRange(14, 15))
        val endHour = currentEvent!!.endAt.slice(IntRange(11, 12))
        val endMinute = currentEvent!!.endAt.slice(IntRange(14, 15))
        startTimePeriod.value = timeFormat(startHour, startMinute)
        endTimePeriod.value = timeFormat(endHour, endMinute)
        timePickerValueStartTime = startHour.toInt()
        timePickerValueStartMinute = startMinute.toInt()
        timePickerValueEndTime = endHour.toInt()
        timePickerValueEndMinute = endMinute.toInt()
    }
}