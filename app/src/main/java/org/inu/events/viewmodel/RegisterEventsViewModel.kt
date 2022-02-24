package org.inu.events.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
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
import java.io.File
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
    val phase = MutableLiveData(0)
    val title = MutableLiveData("")
    val body = MutableLiveData("")
    val host = MutableLiveData("")
    val target = MutableLiveData("") //todo - 서버에 필드 추가되면 구현
    val submissionUrl = MutableLiveData("")
    val contactNumber = MutableLiveData("")
    val imageUrl = MutableLiveData("")
    val imageCheckBoxBoolean = MutableLiveData(false)
    val timeCheckBoxBoolean = MutableLiveData(false)
    val contactNumberCheckBoxBoolean = MutableLiveData(false)
    val urlCheckBoxBoolean = MutableLiveData(false)
    val errorMessage = MutableLiveData("")
    private var imageUuid: String? = ""

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
    val checkBoxClickEvent = SingleLiveEvent<Any>()
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

    fun errorMessageString(): Boolean{
        errorMessage.value = when{
            title.value!!.isEmpty() -> "공백은 제목으로 설정하실 수 없습니다."
            else -> ""
        }
        return errorMessage.value!!.isEmpty()
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
            imageUuid = currentEvent?.imageUuid
            spinnerSelected()
            datePickerSelect()
            timePickerSelect()
            loadImage()
        }.catch { }
    }

    private fun loadImage() {
        if(imageUrl.value == "")
            imageUrl.value = "http://uniletter.inuappcenter.kr/images/$imageUuid"
    }

    //행사 수정 시 서버에서 받아온 카테고리 이름 문자열을 스피너 선택으로 바꿔주는 함수
    private fun spinnerSelected() {
        val array = context.resources.getStringArray(R.array.classification)
        for (i in array.indices){
            if(currentEvent?.category == array[i]){
                selectedItemPosition.value = i
            }
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
        execute {
            uploadImage()
            eventRepository.postEvent(
                AddEventParams(
                    title = title.value ?: "",
                    host = host.value ?: "",
                    category = spinnerToCategory(),
                    target = target.value ?: "",
                    startAt = datePickerToStartAt(),
                    endAt = datePickerToEndAt(),
                    contact = contactNumber.value ?: "",
                    location = submissionUrl.value ?: "",
                    body = body.value ?: "",
                    imageUuid = imageUuid ?: ""
                )
            )
        }.then{ }.catch{ }
    }

    private fun updateEvent() {
        execute {
            uploadImage()
            eventRepository.updateEvent(
                currentEvent!!.id,
                UpdateEventParams(
                    title = title.value ?: "",
                    host = host.value ?: "",
                    category = spinnerToCategory(),
                    target = target.value ?: "",
                    startAt = datePickerToStartAt(),
                    endAt = datePickerToEndAt(),
                    contact = contactNumber.value ?: "",
                    location = submissionUrl.value ?: "",
                    body = body.value ?: "",
                    imageUuid = imageUuid ?: ""
                )
            )
            Log.d("tag","서버에 데이터 넣기")
        }.then{ }.catch{ }
    }

    private fun uploadImage(){
        if(imageCheckBoxBoolean.value == false){
            val file = File(imageUrl.value.toString())
            val requestFile = file.asRequestBody("multipart/form-data".toMediaType())
            val image = MultipartBody.Part.createFormData("file", file.name, requestFile)
            imageUuid = eventRepository.uploadImage(image).uuid
        }
        else{
            imageUuid = when(selectedItemPosition.value){
                1->"1ec94c4d-284e-6b70-6eba-0ecc1b8dd491"
                2->"1ec94c3f-2c9d-6590-1fd7-cb603aa85e1e"
                3->"1ec94c15-ee15-6f30-8bdd-76769baf2a97"
                4->"1ec94c15-786e-6520-ea08-df4f8c716b04"
                5->"1ec94c49-4fd6-6ca0-1497-d8b902900844"
                6->"1ec94c42-4e1a-6030-9859-6dc8e7afe7df"
                else->""
            }
        }
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
        if(errorMessageString()){
            if (isItNew) {
                addEvent()
            } else {
                updateEvent()
            }
            finishEvent.call()
        }
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

    fun onCheckBoxClick(){
        if(imageCheckBoxBoolean.value!!){
            imageUrl.value = ""
        }
        if(timeCheckBoxBoolean.value!!){
            endDatePeriod.value = startDatePeriod.value
            endTimePeriod.value = endTimePeriod.value
        }
        if(contactNumberCheckBoxBoolean.value!!){
            contactNumber.value = ""
        }
        if(urlCheckBoxBoolean.value!!){
            submissionUrl.value = ""
        }
        checkBoxClickEvent.call()
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
        Log.d("tag", "사용자에게 입력받은 date = $date")
        val year = date.slice(IntRange(0, 3))
        val month = date.slice(IntRange(5, 6))
        val day = date.slice(IntRange(8, 9))
        val amPm = date.slice(IntRange(16, 17))
        val hour = date.slice(IntRange(10, 11))
        val hourStr = if (amPm == "PM") (hour.toInt() + 12) else hour
        val minute = date.slice(IntRange(13, 14))
        val second = "00"
        return "%s-%s-%s %s:%s:%s".format(year, month, day, hourStr, minute, second)
    }

    private fun dateFormat(year: String, month: String, day: String) =
        "%s.%s.%s".format(year, month, day)

    private fun timeFormat(hour: String, minute: String) =
        "%s:%s %s".format(
            if (hour.toInt() > 12) (hour.toInt() - 12).toString() else hour,
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