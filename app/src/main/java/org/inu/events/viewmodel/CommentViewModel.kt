package org.inu.events.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.data.CommentData
import org.inu.events.data.model.Article
import org.inu.events.data.service.CommentService
import org.inu.events.di.AppConfigs
import org.inu.events.util.SingleLiveEvent

class CommentViewModel : ViewModel() {
    private val _commentDataList = MutableLiveData<List<CommentData>>()
    val commentDataList : LiveData<List<CommentData>>
        get() = _commentDataList

    val btnClickEvent = SingleLiveEvent<Any>()
    private val commentService: CommentService = AppConfigs.commentService
    init{
        _commentDataList.value = loadCommentList()
    }
    // item 삽입
    private fun loadCommentList():List<CommentData>{
        return commentService.fetchComment()
    }

    fun onClickBtn(){
        btnClickEvent.call()
    }
}