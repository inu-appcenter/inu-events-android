package org.inu.events.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.common.threading.execute
import org.inu.events.common.util.SingleLiveEvent
import org.inu.events.data.model.dto.AddCommentParams
import org.inu.events.data.model.dto.UpdateCommentParams
import org.inu.events.data.model.entity.Comment
import org.inu.events.data.model.entity.Event
import org.inu.events.data.repository.CommentRepository
import org.inu.events.data.repository.EventRepository
import org.inu.events.service.LoginService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.lang.RuntimeException

class CommentViewModel : ViewModel(), KoinComponent {
    private val eventRepository: EventRepository by inject()
    private val commentRepository: CommentRepository by inject()

    private val _commentDataList = MutableLiveData<List<Comment>>()
    val commentList: LiveData<List<Comment>>
        get() = _commentDataList

    private val _currentEvent = MutableLiveData<Event>()
    val currentEvent: MutableLiveData<Event>
        get() = _currentEvent

    val commentSizeText = MutableLiveData("댓글 0 >")
    val content = MutableLiveData("")
    val phase = MutableLiveData(1)
    private val loginService: LoginService by inject()
    val isLoggedIn = loginService.isLoggedInLiveData

    val btnClickEvent = SingleLiveEvent<Any>()
    val plusBtnClickEvent = SingleLiveEvent<Any>()
    //val postCommentClickEvent = SingleLiveEvent<Any>()
    val arrowDownBtnClickEvent = SingleLiveEvent<Any>()
    val arrowUpBtnClickEvent = SingleLiveEvent<Any>()
    val showToastEvent = SingleLiveEvent<Any>()

    var eventIndex = -1
        private set
    var commentIndex = -1
        private set

    fun load(eventId: Int) {
        eventIndex = eventId
        loadEventsAndComments()
    }

    fun loadEventsAndComments() {
        execute {
            eventRepository.getEvent(eventIndex)
        }.then {
            _currentEvent.value = it
            loadCommentList()
        }.catch { }
    }

    fun deleteComment(callback: () -> Unit) {
        execute {
            commentRepository.deleteComment(commentId = commentIndex)
        }.then {
            loadCommentList(callback)
        }.catch {
        }
    }

    fun updateComment() {
        execute {
            commentRepository.updateComment(
                id = commentIndex,
                UpdateCommentParams(content = content.value ?: "")
            )
        }.then {
            loadCommentList()
        }.catch { }
    }

    fun postComment() {
        execute {
            Log.i("postComment", content.value.toString())
            commentRepository.postComment(
                AddCommentParams(
                    eventId = eventIndex,
                    content = content.value ?: throw RuntimeException("큰일났다!!!!!!!!!!!!!!!!!!!!!")
                )
            )
        }.then {
            deleteText()
            loadCommentList()
        }.catch { }
    }

    private fun loadCommentList(callback: () -> Unit = {}) {
        execute {
            commentRepository.getComments(eventIndex)
        }.then {
            Log.i("SDFSDFSDFDFQERIEQJIFDFSDJKFJSNZZZZ", it.map { it.toString() }.joinToString(", "))
            callback()
            _commentDataList.value = it
            commentSizeText.value = "댓글 ${it.size} "
        }.catch {
        }
    }

    private fun loadDetailData() {
        execute {
            eventRepository.getEvent(eventIndex)
        }.then {
            _currentEvent.value = it
        }.catch { }
    }

    fun onClickBtn() {
        btnClickEvent.call()
    }

    fun deleteText() {
        content.value = ""
    }

    fun showBottomSheet(commentId: Int) {
        commentIndex = commentId
        Log.i("commentIndex showBottom", commentIndex.toString())
        plusBtnClickEvent.call()
    }

    fun onClickArrowDownBtn() {
        arrowDownBtnClickEvent.call()
        phase.value = 2
    }

    fun onClickArrowUpBtn() {
        arrowUpBtnClickEvent.call()
        phase.value = 1
    }

}