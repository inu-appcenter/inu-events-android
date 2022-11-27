package org.inu.events.viewmodel

import android.content.Intent
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import org.inu.events.LoginActivity
import org.inu.events.MyPageActivity
import org.inu.events.ToolbarListener
import org.inu.events.common.threading.execute
import org.inu.events.common.util.SingleLiveEvent
import org.inu.events.data.model.dto.LikeParam
import org.inu.events.data.model.entity.Event
import org.inu.events.data.repository.EventRepository
import org.inu.events.data.repository.LikeRepository
import org.inu.events.data.repository.impl.EventPagingSource
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

    private val pageSize = 10

    val homeDataList: Flow<PagingData<Event>> = Pager(
        config = PagingConfig(pageSize = pageSize),
        pagingSourceFactory = { eventRepository.createEventPageSource() }
    )
        .flow
        .cachedIn(viewModelScope)

    var eventIndex: Int = 0
    private var like = false

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
//        eventPageSource.invalidate()
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