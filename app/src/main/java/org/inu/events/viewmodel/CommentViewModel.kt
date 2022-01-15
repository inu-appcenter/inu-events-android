package org.inu.events.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.inu.events.data.CommentData

class CommentViewModel : ViewModel() {
    private val _commentList = MutableLiveData<ArrayList<CommentData>>()
    init{
        _commentList.value = loadCommentList()
    }

    val commentList : LiveData<ArrayList<CommentData>>
        get() =_commentList

    // item 삽입
    private fun loadCommentList():ArrayList<CommentData>{
        val tmpComment= ArrayList<CommentData>()
        tmpComment.add(CommentData("장희직","롯데리아 vs 맘스터치","https://bimage.interpark.com/partner/goods_image/5/3/7/5/354285375h.jpg"))
        return tmpComment
    }
}