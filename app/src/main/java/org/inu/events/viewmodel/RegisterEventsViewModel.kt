package org.inu.events.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.inu.events.R
import org.inu.events.common.threading.execute
import org.inu.events.common.util.SingleLiveEvent
import org.inu.events.data.model.dto.AddEventParams
import org.inu.events.data.model.dto.UpdateEventParams
import org.inu.events.data.model.entity.Event
import org.inu.events.data.repository.EventRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
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
    val host = MutableLiveData<String?>()
    val target = MutableLiveData<String?>()
    val location = MutableLiveData<String?>()
    val contactNumber = MutableLiveData<String?>()
    val imageUrl = MutableLiveData("")
    val imageCheckBoxBoolean = MutableLiveData(false)
    val timeCheckBoxBoolean = MutableLiveData(false)
    val contactNumberCheckBoxBoolean = MutableLiveData(false)
    val locationCheckBoxBoolean = MutableLiveData(false)
    val errorMessage = MutableLiveData("")

    private val imageUuidList = listOf(null,"1ec94c4d-284e-6b70-6eba-0ecc1b8dd491",
        "1ec94c3f-2c9d-6590-1fd7-cb603aa85e1e",
        "1ec94c15-ee15-6f30-8bdd-76769baf2a97",
        "1ec94c15-786e-6520-ea08-df4f8c716b04",
        "1ec94c49-4fd6-6ca0-1497-d8b902900844",
        "1ec94c42-4e1a-6030-9859-6dc8e7afe7df",
        null
    )
    private var imageUuid: String? = ""
    private var imageTmp: String? = ""
    private var contactTmp: String? = ""
    private var urlTmp: String? = ""
    private var dateTmp: String? = ""
    private var timeTmp: String? = ""

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
            title.value = it.title
            body.value = it.body
            host.value = it.host
            target.value = it.target
            contactNumber.value = it.contact
            location.value = it.location
            imageUuid = currentEvent?.imageUuid
            spinnerSelected()
            datePickerSelect()
            timePickerSelect()
            loadImage()
            loadCheckBoxState()
        }.catch { }
    }

    private fun loadCheckBoxState() {
        if(currentEvent?.location == null){
            locationCheckBoxBoolean.value = true
        }
        if(currentEvent?.contact == null){
            contactNumberCheckBoxBoolean.value = true
        }
        if(currentEvent?.startAt == currentEvent?.endAt){
            timeCheckBoxBoolean.value = true
        }
        val booleanImageDefault = imageUuidList.contains(currentEvent?.imageUuid)
        if(booleanImageDefault){
            imageCheckBoxBoolean.value = true
        }
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
        return formatDateForServer("${startDatePeriod.value!!} ${startTimePeriod.value!!}")
    }

    private fun datePickerToEndAt(): String {
        return formatDateForServer("${endDatePeriod.value!!} ${endTimePeriod.value!!}")
    }

    private fun addEvent() {
        execute {
            uploadImage()
            eventRepository.postEvent(
                AddEventParams(
                    title = title.value ?: "",
                    host = host.value,
                    category = spinnerToCategory(),
                    target = target.value,
                    startAt = datePickerToStartAt(),
                    endAt = datePickerToEndAt(),
                    contact = contactNumber.value,
                    location = location.value,
                    body = body.value ?: "",
                    imageUuid = imageUuid
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
                    host = host.value,
                    category = spinnerToCategory(),
                    target = target.value,
                    startAt = datePickerToStartAt(),
                    endAt = datePickerToEndAt(),
                    contact = contactNumber.value,
                    location = location.value,
                    body = body.value ?: "",
                    imageUuid = imageUuid
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
            imageUuid = imageUuidList[selectedItemPosition.value!!]
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

    fun onLocationCheckBoxClick(){
        when{
            locationCheckBoxBoolean.value!! ->{
                urlTmp = location.value
                location.value = null
            }
            !(locationCheckBoxBoolean.value!!) -> location.value = urlTmp
        }
    }

    fun onImageCheckBoxClick(){
        when{
            imageCheckBoxBoolean.value!! -> {
                imageTmp = imageUrl.value!!
                imageUrl.value = null
            }
            !(imageCheckBoxBoolean.value!!) -> imageUrl.value = imageTmp
        }
    }

    fun onTimeCheckBoxClick(){
        when{
            timeCheckBoxBoolean.value!! -> {
                dateTmp = endDatePeriod.value
                timeTmp = endTimePeriod.value
                endDatePeriod.value = startDatePeriod.value
                endTimePeriod.value = startTimePeriod.value
            }
            !(timeCheckBoxBoolean.value!!) ->{
                endDatePeriod.value = dateTmp
                endTimePeriod.value = timeTmp
            }
        }
    }

    fun onContactNumberCheckBoxClick() {
        when {
            contactNumberCheckBoxBoolean.value!! -> {
                contactTmp = contactNumber.value
                contactNumber.value = null
            }
            !(contactNumberCheckBoxBoolean.value!!) -> contactNumber.value = contactTmp
        }
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

    private fun serverDateToString(date: String): String{
        val stringDate:LocalDate = LocalDate.parse(date, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        return stringDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
    }

    private fun serverTimeToString(time: String): String{
        val timeDate:LocalTime = LocalTime.parse(time, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        return timeDate.format(DateTimeFormatter.ofPattern("hh:mm a",Locale("en", "US")))
    }

    private fun formatDateForServer(date: String): String{
        val serverDate:LocalDateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm a", Locale("en", "US")))
        return serverDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    private fun datePickerSelect() {
        startDatePeriod.value = serverDateToString(currentEvent!!.startAt)
        endDatePeriod.value = serverDateToString(currentEvent!!.endAt)
        datePickerValueStartYear = startDatePeriod.value!!.slice(IntRange(0,3)).toInt()
        datePickerValueStartMonth = startDatePeriod.value!!.slice(IntRange(5,6)).toInt()
        datePickerValueStartDay = startDatePeriod.value!!.slice(IntRange(8,9)).toInt()
        datePickerValueEndYear = endDatePeriod.value!!.slice(IntRange(0,3)).toInt()
        datePickerValueEndMonth = endDatePeriod.value!!.slice(IntRange(5,6)).toInt()
        datePickerValueEndDay = endDatePeriod.value!!.slice(IntRange(8,9)).toInt()
    }

    private fun timePickerSelect() {    //행사 수정 시 서버에서 받아온 시간 값을 textView에 표시하기 위한 함수
        startTimePeriod.value = serverTimeToString(currentEvent!!.startAt)
        endTimePeriod.value = serverTimeToString(currentEvent!!.endAt)
        timePickerValueStartTime = startTimePeriod.value!!.slice(IntRange(0,1)).toInt()
        timePickerValueStartMinute = startTimePeriod.value!!.slice(IntRange(3,4)).toInt()
        timePickerValueEndTime = endTimePeriod.value!!.slice(IntRange(0,1)).toInt()
        timePickerValueEndMinute = endTimePeriod.value!!.slice(IntRange(3,4)).toInt()
    }
}