package org.inu.events.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.data.CommentData
import org.inu.events.data.model.dto.AddCommentParams
import org.inu.events.data.service.CommentService
import org.inu.events.di.AppConfigs
import org.inu.events.util.SingleLiveEvent

class CommentViewModel : ViewModel() {
    private val _commentDataList = MutableLiveData<List<CommentData>>()
    val commentDataList: LiveData<List<CommentData>>
        get() = _commentDataList

    val commentSizeText = MutableLiveData("댓글 5 >")
    val content = MutableLiveData("")

    val btnClickEvent = SingleLiveEvent<Any>()
    private val commentService: CommentService = AppConfigs.commentService

    var eventIndex = -1
        private set

    fun load(eventId: Int) {
        eventIndex = eventId
        loadCommentList()
    }

    fun deleteComment(commentId: Int) {
        commentService.deleteComment(commentId)
        loadCommentList()
    }

    fun postComment() {
        commentService.addComment(
            AddCommentParams(
                eventId = eventIndex,
                content = content.value ?: ""
            )
        )
        loadCommentList()
    }

    // item 삽입
    private fun loadCommentList() {
        val comments = commentService.fetchComments(eventIndex)
        _commentDataList.value = comments
        commentSizeText.value = "댓글 ${comments.size} >"
    }

    fun onClickBtn() {
        btnClickEvent.call()
    }
}