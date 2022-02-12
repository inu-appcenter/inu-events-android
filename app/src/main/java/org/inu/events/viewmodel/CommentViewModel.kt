package org.inu.events.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.common.threading.execute
import org.inu.events.common.util.SingleLiveEvent
import org.inu.events.data.model.dto.AddCommentParams
import org.inu.events.data.model.entity.Comment
import org.inu.events.data.repository.CommentRepository
import org.inu.events.service.LoginService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CommentViewModel : ViewModel(), KoinComponent {
    private val commentRepository: CommentRepository by inject()

    private val _commentDataList = MutableLiveData<List<Comment>>()
    val commentList: LiveData<List<Comment>>
        get() = _commentDataList

    val commentSizeText = MutableLiveData("댓글 5 >")
    val content = MutableLiveData("")
    private val loginService: LoginService by inject()
    val isLoggedIn = loginService.isLoggedInLiveData

    val btnClickEvent = SingleLiveEvent<Any>()

    var eventIndex = -1
        private set

    fun load(eventId: Int) {
        eventIndex = eventId
        loadCommentList()
    }

    fun deleteComment(commentId: Int) {
        execute {
            commentRepository.deleteComment(commentId)
        }.then {
            loadCommentList()
        }.catch { }
    }

    fun postComment() {
        execute {
            commentRepository.postComment(
                AddCommentParams(
                    eventId = eventIndex,
                    content = content.value ?: ""
                )
            )
        }.then {
            loadCommentList()
        }.catch { }
    }

    private fun loadCommentList() {
        execute {
            commentRepository.getComments(eventIndex)
        }.then {
            _commentDataList.value = it
            commentSizeText.value = "댓글 ${it.size} >"
        }.catch { }
    }

    fun onClickBtn() {
        btnClickEvent.call()
    }
}