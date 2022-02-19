package org.inu.events.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.BottomSheet
import org.inu.events.common.threading.execute
import org.inu.events.common.util.SingleLiveEvent
import org.inu.events.data.model.dto.AddCommentParams
import org.inu.events.data.model.dto.UpdateCommentParams
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

    val commentSizeText = MutableLiveData("댓글 0 >")
    val content = MutableLiveData("")
    private val loginService: LoginService by inject()
    val isLoggedIn = loginService.isLoggedInLiveData

    val btnClickEvent = SingleLiveEvent<Any>()
    val plusBtnClickEvent = SingleLiveEvent<Any>()

    var eventIndex = -1
        private set
    var commentIndex = -1
        private set

    fun load(eventId: Int) {
        eventIndex = eventId
        loadCommentList()
    }

    fun deleteComment(callback: () -> Unit) {
        execute {
            commentRepository.deleteComment(commentId = commentIndex)
        }.then {
            loadCommentList(callback)
        }.catch { }
    }

    fun updateComment() {
        execute {
            commentRepository.updateComment(id = commentIndex,
                UpdateCommentParams(content = content.value ?: "")
            )
        }.then {
            // todo - 이거 하는건지 체크
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

    private fun loadCommentList(callback: () -> Unit = {}) {
        execute {
            commentRepository.getComments(eventIndex)
        }.then {
            Log.i("SDFSDFSDFDFQERIEQJIFDFSDJKFJSNZZZZ",it.map { it.toString() }.joinToString(", "))
            callback()
            _commentDataList.value = it
            commentSizeText.value = "댓글 ${it.size} >"
        }.catch {
            it.printStackTrace()
        }
    }

    fun onClickBtn() {
        btnClickEvent.call()
    }

    fun showBottomSheet(commentId: Int) {
        commentIndex = commentId
        plusBtnClickEvent.call()
    }
}