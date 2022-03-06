package org.inu.events.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.inu.events.R
import org.inu.events.common.threading.execute
import org.inu.events.common.util.Period
import org.inu.events.common.util.SingleLiveEvent
import org.inu.events.data.model.dto.AddEventParams
import org.inu.events.data.model.dto.UpdateEventParams
import org.inu.events.data.model.entity.Event
import org.inu.events.data.repository.EventRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class TempRegisterViewModel : ViewModel(), KoinComponent {
    private val context: Context by inject()
    private val eventRepository: EventRepository by inject()

    val onPreviousEvent = SingleLiveEvent<Any>()
    val onNextEvent = SingleLiveEvent<Any>()

    val selectedItemPosition = MutableLiveData(0)
    val period = Period()
    val content = MutableLiveData("")
    val title = MutableLiveData("")
    val body = MutableLiveData("")
    val host = MutableLiveData<String?>()
    val target = MutableLiveData("")
    val location = MutableLiveData<String?>()
    val contactNumber = MutableLiveData<String?>()
    val imageUrl = MutableLiveData("")
    val imageCheckBoxBoolean = MutableLiveData(false)
    val sameCheckBoxBoolean = MutableLiveData(false)
    val hostCheckBoxBoolean = MutableLiveData(false)
    val contactNumberCheckBoxBoolean = MutableLiveData(false)
    val locationCheckBoxBoolean = MutableLiveData(false)
    val titleEditTextEmpty = MutableLiveData(false)
    val targetEditTextEmpty = MutableLiveData(false)

    private val imageUuidList = listOf("1ec96f4e-970e-6780-792a-5dc26eec006c",
        "1ec94c4d-284e-6b70-6eba-0ecc1b8dd491",
        "1ec94c3f-2c9d-6590-1fd7-cb603aa85e1e",
        "1ec94c15-ee15-6f30-8bdd-76769baf2a97",
        "1ec94c15-786e-6520-ea08-df4f8c716b04",
        "1ec94c49-4fd6-6ca0-1497-d8b902900844",
        "1ec94c42-4e1a-6030-9859-6dc8e7afe7df",
        "1ec96f4e-970e-6780-792a-5dc26eec006c"
    )
    private var today = LocalDateTime.now()
    private var imageUuid: String? = ""
    private var imageTmp: String? = ""
    private var contactTmp: String? = ""
    private var urlTmp: String? = ""
    private var dateTmp: String? = today.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
    private var timeTmp: String? = today.format(DateTimeFormatter.ofPattern("hh:mm a",Locale("en","US")))
    private var hostTmp: String? = ""

    //기존 글 수정 시 타임피커와 데이트피커 값을 불러오기 위한 정보 저장 변수
    private val cal = Calendar.getInstance()
    var datePickerValueStartYear = cal.get(Calendar.YEAR)
    var datePickerValueStartMonth = cal.get(Calendar.MONTH)+1
    var datePickerValueStartDay = cal.get(Calendar.DATE)
    var datePickerValueEndYear = cal.get(Calendar.YEAR)
    var datePickerValueEndMonth = cal.get(Calendar.MONTH)+1
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
            title.value = it.title
            body.value = it.body
            host.value = it.host
            target.value = it.target
            contactNumber.value = it.contact
            location.value = it.location
            imageUuid = it.imageUuid
            spinnerSelected()
            datePickerSelect()
            timePickerSelect()
            loadImage()
            loadCheckBoxState()
        }.catch { }
    }

    private fun loadCheckBoxState() {
        if(currentEvent?.host == null){
            hostCheckBoxBoolean.value = true
        }
        if(currentEvent?.location == null){
            locationCheckBoxBoolean.value = true
        }
        if(currentEvent?.contact == null){
            contactNumberCheckBoxBoolean.value = true
        }
        if(currentEvent?.startAt == currentEvent?.endAt){
            sameCheckBoxBoolean.value = true
        }
        val booleanImageDefault = imageUuidList.contains(currentEvent?.imageUuid)
        if(booleanImageDefault){
            imageCheckBoxBoolean.value = true
        }
    }

    fun onPrevious() {
        onPreviousEvent.call()
    }

    fun onClickNext() {
        onNextEvent.call()
    }

    private fun loadImage() {
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

    fun onCompleteClick() {
        // TODO : 필수 항목 작성 여부 체크

        if (isItNew) {
            addEvent()
        } else {
            updateEvent()
        }
    }

    private fun addEvent() {
        noImage()
        execute {
            eventRepository.postEvent(
                AddEventParams(
                    title = title.value ?: "",
                    host = host.value,
                    category = spinnerToCategory(),
                    target = target.value,
                    startAt = period.formattedStartDateTime,
                    endAt = period.formattedEndDateTime,
                    contact = if(contactNumber.value.isNullOrBlank())null else contactNumber.value,
                    location = if(location.value.isNullOrBlank())null else location.value,
                    body = body.value ?: "",
                    imageUuid = imageUuid
                )
            )
        }.then{
        }.catch{ }
    }

    private fun updateEvent() {
        noImage()
        execute {
            eventRepository.updateEvent(
                currentEvent!!.id,
                UpdateEventParams(
                    title = title.value ?: "",
                    host = host.value,
                    category = spinnerToCategory(),
                    target = target.value,
                    startAt = period.formattedStartDateTime,
                    endAt = period.formattedEndDateTime,
                    contact = if(contactNumber.value.isNullOrBlank())null else contactNumber.value,
                    location = if(location.value.isNullOrBlank())null else location.value,
                    body = body.value ?: "",
                    imageUuid = imageUuid
                )
            )
        }.then{
        }.catch{ }
    }

    private fun noImage(){
        if(imageUuid == "") imageCheckBoxBoolean.value = true
        if(imageCheckBoxBoolean.value == true){
            imageUuid = imageUuidList[selectedItemPosition.value!!]
        }
    }

    private fun uploadImage(){
        if (imageCheckBoxBoolean.value == false){
            val file = File(imageUrl.value.toString())
            val requestFile = file.asRequestBody("multipart/form-data".toMediaType())
            val image = MultipartBody.Part.createFormData("file", file.name, requestFile)
            imageUuid = eventRepository.uploadImage(image).uuid
        }
    }

    fun onImageButtonClick() {
        startGalleryClickEvent.call()
    }

    fun updateImage(){
        execute {
            uploadImage()
        }.catch {  }.then {  }
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
                imageUrl.value = ""
            }
            !(imageCheckBoxBoolean.value!!) -> imageUrl.value = imageTmp
        }
    }

    fun onSameCheckBoxClick(){      //위와 동일 체크
        when{
            sameCheckBoxBoolean.value!! -> {
                dateTmp = period.endDate.value
                timeTmp = period.endTime.value
                period.endDate.value = period.startDate.value
                period.endTime.value = period.startTime.value
            }
            !(sameCheckBoxBoolean.value!!) ->{
                period.endDate.value = dateTmp
                period.endTime.value = timeTmp
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

    fun onHostCheckBoxClick() {
        when {
            hostCheckBoxBoolean.value!! -> {
                hostTmp = host.value
                host.value = null
            }
            !(hostCheckBoxBoolean.value!!) -> host.value = hostTmp
        }
    }

    private fun datePickerSelect() {
        period.startDate.value = period.serverDateToString(currentEvent!!.startAt)
        period.endDate.value = period.serverDateToString(currentEvent!!.endAt)
        datePickerValueStartYear = period.startDate.value!!.slice(IntRange(0,3)).toInt()
        datePickerValueStartMonth = period.startDate.value!!.slice(IntRange(5,6)).toInt()
        datePickerValueStartDay = period.startDate.value!!.slice(IntRange(8,9)).toInt()
        datePickerValueEndYear = period.endDate.value!!.slice(IntRange(0,3)).toInt()
        datePickerValueEndMonth = period.endDate.value!!.slice(IntRange(5,6)).toInt()
        datePickerValueEndDay = period.endDate.value!!.slice(IntRange(8,9)).toInt()
    }

    private fun timePickerSelect() {    //행사 수정 시 서버에서 받아온 시간 값을 textView에 표시하기 위한 함수
        period.startTime.value = period.serverTimeToString(currentEvent!!.startAt)
        period.endTime.value = period.serverTimeToString(currentEvent!!.endAt)
        timePickerValueStartTime = period.startTime.value!!.slice(IntRange(0,1)).toInt()
        timePickerValueStartMinute = period.startTime.value!!.slice(IntRange(3,4)).toInt()
        timePickerValueEndTime = period.endTime.value!!.slice(IntRange(0,1)).toInt()
        timePickerValueEndMinute = period.endTime.value!!.slice(IntRange(3,4)).toInt()
    }

    fun setupCurrentTime() {
        period.setupCurrentTime()
    }
}