package org.inu.events.ui.home

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.inu.events.common.threading.execute
import org.inu.events.common.util.SingleLiveEvent
import org.inu.events.data.model.dto.LikeParam
import org.inu.events.data.model.entity.Event
import org.inu.events.data.repository.EventRepository
import org.inu.events.data.repository.LikeRepository
import org.inu.events.service.LoginService
import org.inu.events.ui.login.LoginActivity
import org.inu.events.ui.mypage.MyPageActivity
import org.inu.events.ui.util.dialog.LoginDialog
import org.inu.events.ui.util.toolbar.ToolbarListener
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeViewModel : ViewModel(), KoinComponent {

    private val eventRepository: EventRepository by inject()
    private val loginService: LoginService by inject()
    private val likeRepository: LikeRepository by inject()

    private val _homeDataList = MutableStateFlow<PagingData<Event>>(PagingData.empty())
    val homeDataList = _homeDataList.asStateFlow()
    private var categoryId = 0
    private var eventStatus = false

    val postClickEvent = SingleLiveEvent<Any>()
    val likeClickEvent = SingleLiveEvent<Any>()
    val shouldRefresh = SingleLiveEvent<Any>()
    val toolbarListener = object : ToolbarListener {
        override fun onClickSearch(view: View) {
            val intent = Intent(view.context, SearchActivity::class.java)
            view.context.startActivity(intent)
        }

        override fun onClickMyPage(view: View) {
            if (loginService.isLoggedIn) {
                val intent = Intent(view.context, MyPageActivity::class.java)
                view.context.startActivity(intent)
            } else {
                val intent = Intent(view.context, LoginActivity::class.java)
                LoginDialog().show(view.context,
                    { view.context.startActivity(intent) },
                    { }
                )
            }
        }
    }

    init {
        getHomeData(categoryId, eventStatus)
    }

    fun refresh() {
        shouldRefresh.call()
    }

    fun onClickPost() {
        postClickEvent.call()
    }

    fun onLogIn() {
        likeClickEvent.call()
    }

    fun onLikePost(event: Event): Boolean {
        when (event.likedByMe) {
            true -> deleteLike(event.id)
            else -> postLike(event.id)
        }
        return loginService.isLoggedIn
    }

    private fun postLike(eventId: Int) {
        execute {
            likeRepository.postLike(
                LikeParam(eventId = eventId)
            )
        }.then {
        }.catch { }
    }

    private fun deleteLike(eventId: Int) {
        execute {
            likeRepository.deleteLike(
                LikeParam(eventId = eventId)
            )
        }.then {
        }.catch { }
    }

    private fun getHomeData(categoryId: Int, eventStatus: Boolean) {
        viewModelScope.launch {
            eventRepository.getEvents(categoryId, eventStatus).collect {
                _homeDataList.value = it
            }
        }
    }

    val onCategoryItemClick = fun(position: Int) {
        categoryId = position
        getHomeData(categoryId, eventStatus)
    }

    val onFilterItemClick = fun(position: Int) {
        eventStatus = position == 1
        getHomeData(categoryId, eventStatus)
    }
}