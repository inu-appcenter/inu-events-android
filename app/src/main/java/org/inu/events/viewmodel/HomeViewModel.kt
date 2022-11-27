package org.inu.events.viewmodel

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import org.inu.events.LoginActivity
import org.inu.events.MyPageActivity
import org.inu.events.R
import org.inu.events.ToolbarListener
import org.inu.events.common.threading.execute
import org.inu.events.common.util.SingleLiveEvent
import org.inu.events.data.model.dto.GetEventByCategoryParam
import org.inu.events.data.model.dto.LikeParam
import org.inu.events.data.model.entity.Event
import org.inu.events.data.repository.EventRepository
import org.inu.events.data.repository.LikeRepository
import org.inu.events.dialog.LoginDialog
import org.inu.events.service.LoginService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeViewModel : ViewModel(), KoinComponent {

    private val eventRepository: EventRepository by inject()
    private val loginService: LoginService by inject()
    private val likeRepository: LikeRepository by inject()

    private val _homeDataList = MutableLiveData<List<Event>>()
    val homeDataList: LiveData<List<Event>>
        get() = _homeDataList

    var eventIndex: Int = 0
    private var like = false
    var selectedCategory = MutableLiveData(0)
    private val eventQueryParam = MutableLiveData(
        GetEventByCategoryParam(
            categoryId = 0,
            eventStatus = false,
            pageNum = 0,
            pageSize = 0
        )
    )

    val postClickEvent = SingleLiveEvent<Any>()
    val likeClickEvent = SingleLiveEvent<Any>()
    val spinnerClickEvent = SingleLiveEvent<Any>()
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
        eventQueryParam.value?.let {
            it.categoryId = selectedCategory.value?.toInt() ?: 0
            loadEventByCategory(
                params = it
            )
        }
    }

    @Deprecated("이벤트 전부 불러오는 메서드. 카테고리 필터링으로 대체할 예정임")
    private fun loadHomeData() {
        execute {
            eventRepository.getEvents()
        }.then {
            _homeDataList.value = it
            spinnerClickEvent.call()
        }.catch {  }
    }

    private fun loadEventByCategory(params: GetEventByCategoryParam){
        execute {
            eventRepository.getEventByCategory(params)
        }.then{
            _homeDataList.value = it
            spinnerClickEvent.call()
        }.catch {  }
    }

    fun onClickPost() {
        postClickEvent.call()
    }

    fun onLogIn(){
        likeClickEvent.call()
    }

    fun onLikePost():Boolean{
        when{
            like -> deleteLike(eventIndex)
            else -> postLike(eventIndex)
        }
        return loginService.isLoggedIn
    }

    fun onClickSpinner(){
//        spinnerClickEvent.call()
        load()
    }

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