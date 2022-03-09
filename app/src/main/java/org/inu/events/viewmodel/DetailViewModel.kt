package org.inu.events.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.R
import org.inu.events.common.extension.toast
import org.inu.events.common.threading.execute
import org.inu.events.common.util.Period
import org.inu.events.data.model.entity.Event
import org.inu.events.common.util.SingleLiveEvent
import org.inu.events.data.model.dto.AddBlockParams
import org.inu.events.data.model.dto.LikeParam
import org.inu.events.data.model.dto.NotificationParams
import org.inu.events.data.repository.BlockRepository
import org.inu.events.data.repository.EventRepository
import org.inu.events.data.repository.LikeRepository
import org.inu.events.data.repository.NotificationRepository
import org.inu.events.service.LoginService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

class DetailViewModel : ViewModel(), KoinComponent {
    private val eventRepository: EventRepository by inject()
    private val notificationRepository: NotificationRepository by inject()
    private val likeRepository: LikeRepository by inject()
    private val loginService: LoginService by inject()
    private val blockRepository: BlockRepository by inject()
    private val context: Context by inject()

    //현재 표시할 게시물의 데이터가 저장돼있음
    private val _currentEvent = MutableLiveData<Event>()
    val currentEvent: MutableLiveData<Event>
        get() = _currentEvent

    var imageUrl = MutableLiveData("")
    var startDate = MutableLiveData("")
    var endDate = MutableLiveData("")
    var startTime = MutableLiveData("")
    var endTime = MutableLiveData("")
    var createdDate = MutableLiveData("")
    val notificationOnOff = MutableLiveData(false)
    val like = MutableLiveData(0)
    val likeOnOff = MutableLiveData(false)
    private val notificationSetFor = MutableLiveData("")
    private val period = Period()

    var eventIndex = -1
        private set
    var eventWroteByMeBoolean = false
        private set

    val commentClickEvent = SingleLiveEvent<Int>()
    val alarmClickEvent = SingleLiveEvent<Int>()
    val menuClickEvent = SingleLiveEvent<Any>()
    val userMenuClickEvent = SingleLiveEvent<Any>()
    val notificationText = MutableLiveData<String>()
    val notificationColor = MutableLiveData<Int>(R.color.black80)
    val notificationBackground = MutableLiveData<Int>(R.drawable.notification_on_btn_background)
    val locationNull = MutableLiveData(false)
    val contactNull = MutableLiveData(false)
    val hostNull = MutableLiveData(false)
    val boardDateText = MutableLiveData("D-??")
    var notificationQuarter = MutableLiveData(-1)   // 알림이 어떻게 표시되어야 하는지 구분 하기위한 변수
    val deadLine = MutableLiveData(false)


    fun load(eventId: Int) {
        eventIndex = eventId
        loadDetailData()
    }

    //현재 표시할 게시물의 데이터를 가져옴
    private fun loadDetailData() {
        execute {
            eventRepository.getEvent(eventIndex)
        }.then {
            _currentEvent.value = it
            startDate.value = period.serverDateToString(it.startAt)
            endDate.value = period.serverDateToString(it.endAt)
            startTime.value = period.serverTimeToString(it.startAt)
            endTime.value = period.serverTimeToString(it.endAt)
            createdDate.value = period.serverDateToString(it.createdAt)
            imageUrl.value = "http://uniletter.inuappcenter.kr/images/${it.imageUuid}"
            locationNull.value = (it.location == null)
            contactNull.value = (it.contact == null)
            hostNull.value = (it.host == null)
            like.value = it.likes
            eventWroteByMeBoolean = it.wroteByMe ?:false
            notificationQuarter.value = timeComparison(LocalDateTime.now().toString(),it.startAt,it.endAt)
            notificationOnOff.value = it.notificationSetByMe ?: false
            notificationText.value = if(notificationQuarter.value != 0){ if (notificationOnOff.value!!) "알람 취소" else "알람 신청"} else "행사 마감"
            notificationColor.value = if (notificationQuarter.value != 0 ) {if (notificationOnOff.value!!) R.color.primary100 else R.color.white} else R.color.black
            notificationBackground.value = if (notificationQuarter.value != 0 ) {if (notificationOnOff.value!!) R.drawable.notification_off_btn_background else R.drawable.notification_on_btn_background} else R.drawable.btn_background_black10_radius6
            notificationSetFor.value = it.notificationSetFor
            boardDateText.value = period.whenDay(it.endAt)
            deadLine.value = period.checkDeadline
            likeOnOff.value = it.likedByMe
        }.catch {
            Log.i("error: loadDetailData",it.stackTrace.toString())
        }

    }

    private fun loadNotificationButton(onOff:Boolean){
        notificationText.value = if (onOff) "알림 취소" else "알림 신청"
        notificationColor.value = if (onOff) R.color.primary100 else R.color.white
        notificationBackground.value = if (onOff) R.drawable.notification_off_btn_background else R.drawable.notification_on_btn_background
    }

    //댓글버튼 클릭했을 때 이벤트
    fun onClickComment() {
        commentClickEvent.value = eventIndex
    }

    // 알람버튼 클릭했을 때 이벤트
    fun onClickNotification(){
        alarmClickEvent.value = notificationQuarter.value
    }

    // 좋아요버튼 클릭했을 때 이벤트
    fun onClickLike(){
        if (loginService.isLoggedIn) {
            if (likeOnOff.value == true) {
                deleteLike()
            } else {
                postLike()
            }
            likeOnOff.value = likeOnOff.value != true
        }
        else{
            context.toast("로그인을 하셔야 저장하실 수 있습니다~!!")
        }
    }

    fun blockUser() {
        execute {
            blockRepository.postBlockUser(
                AddBlockParams(
                    targetUserId = _currentEvent.value!!.userId
                )
            )
        }.then {
        }.catch { it.printStackTrace() }
    }

    fun onClickMenu(){
        menuClickEvent.call()
    }

    fun onClickUserMenu(){
        userMenuClickEvent.call()
    }

    // 게시물의 시작시간과 마감시간 또 현재 시간을 비교하는 함수
    private fun timeComparison(now:String,startDate:String,endDate:String): Int{
        return when (startDate) {
            endDate -> when {       // 시작시간과 마감시간이 같은 행사
                now < startDate -> 1   // 지금이 시작 전이라면 시작 전 알림만
                else -> 0  // 지금이 시작한 후라면 비활성화
            }
            else -> when {   // 시작시간과 마감시간이 다른 행사
                now < startDate -> 3     // 지금이 시작 전이라면 시작전과 마감알림 모두
                startDate <= now && now < endDate -> 2  // 지금이 행사 시작후, 마감 전이라면 마감 전 알림만
                else -> 0   // 지금이 마감뒤라면 비활성화
            }
        }
    }

    private fun postLike(){
        execute {
            likeRepository.postLike(
                LikeParam(eventId = eventIndex)
            )
        }.then { like.value = like.value!! + 1
        }.catch {  }
    }

    fun deleteLike(){
        execute {
            likeRepository.deleteLike(
                LikeParam(eventId = eventIndex)
            )
        }.then { like.value = like.value!! - 1
        }.catch {  }
    }

    fun postNotification(setFor:String){
        execute{
            notificationRepository.postNotification(
                NotificationParams(
                    eventId = eventIndex,
                    setFor = setFor
                )
            )
        }.then {
            notificationOnOff.value = true
            this.notificationSetFor.value = setFor
            loadNotificationButton(notificationOnOff.value!!)
        }.catch {
        }
    }

    fun deleteNotification(){
        execute {
            notificationRepository.deleteNotification(
                NotificationParams(
                    eventId = eventIndex,
                    setFor = notificationSetFor.value!!
                )
            )
        }.then {
            notificationOnOff.value = false
            loadNotificationButton(notificationOnOff.value!!)
        }.catch {
        }
    }

    fun getNotifications(){
        execute {
            notificationRepository.getNotifications()
        }.then {
        }.catch {
        }
    }

    fun onDeleteClickEvent() {
        execute {
            eventRepository.deleteEvent(eventIndex)
        }.then {
        }. catch {  }
    }

}