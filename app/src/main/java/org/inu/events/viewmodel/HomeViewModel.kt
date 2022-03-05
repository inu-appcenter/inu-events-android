package org.inu.events.viewmodel

import android.content.Intent
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.LoginActivity
import org.inu.events.MyPageActivity
import org.inu.events.ToolbarListener
import org.inu.events.common.threading.execute
import org.inu.events.common.util.SingleLiveEvent
import org.inu.events.data.model.dto.LikeParam
import org.inu.events.data.model.entity.Event
import org.inu.events.data.repository.EventRepository
import org.inu.events.data.repository.LikeRepository
import org.inu.events.dialog.LoginDialog
import org.inu.events.service.LoginService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : ViewModel(), KoinComponent {

    private val eventRepository: EventRepository by inject()
    private val loginService: LoginService by inject()
    private val likeRepository: LikeRepository by inject()

    private val _homeDataList = MutableLiveData<List<Event>>()
    val homeDataList: LiveData<List<Event>>
        get() = _homeDataList

    private var eventIndex: Int = 0
    private var like = false
    private var checkDeadline = false

    val postClickEvent = SingleLiveEvent<Any>()
    val likeClickEvent = SingleLiveEvent<Any>()
    val toolbarListener = object : ToolbarListener {
        override fun onClickMyPage(view: View) {
            if(loginService.isLoggedIn) {
                val intent = Intent(view.context, MyPageActivity::class.java)
                view.context.startActivity(intent)
            }
            else {
                val intent = Intent(view.context, LoginActivity::class.java)
                LoginDialog().show(view.context,
                    { view.context.startActivity(intent) },
                    { } )
            }
        }
    }

    fun load() {
        loadHomeData()
    }

    private fun loadHomeData() {
        execute {
            eventRepository.getEvents()
        }.then {
            _homeDataList.value = it
        }.catch {  }
    }

    fun onClickPost() {
        postClickEvent.call()
    }

    fun onLikeClickEvent(likeByMe: Boolean,eventId: Int){
        like = likeByMe
        eventIndex = eventId
        likeClickEvent.call()
    }

    fun onLikePost():Boolean{
        when{
            like -> deleteLike(eventIndex)
            else -> postLike(eventIndex)
        }
        return loginService.isLoggedIn
    }

    fun whenDay(end_at: String?): String {
        if (end_at == null) return "D-??"

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        val endDate = dateFormat.parse(end_at).time
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time.time

        val dDay = (endDate - today) / (24 * 60 * 60 * 1000)

        if (dDay < 0) {
            checkDeadline = true
            return "마감"
        }
        checkDeadline = false
        return "D-${if(dDay.toInt() == 0) "day" else dDay}"
    }

    fun onDeadLineCheck() = checkDeadline

    private fun postLike(eventId: Int){
        execute {
            likeRepository.postLike(
                LikeParam(eventId = eventId)
            )
        }.then {
        }.catch {  }
    }

    private fun deleteLike(eventId: Int){
        execute {
            likeRepository.deleteLike(
                LikeParam(eventId = eventId)
            )
        }.then {
        }.catch {  }
    }
}