package org.inu.events.data.service

import org.inu.events.data.CommentData
import org.inu.events.data.model.dto.AddCommentParams
import retrofit2.Call

class DummyCommentService: CommentService {
    private var commentList: List<CommentData> = listOf(CommentData(1,3,3,"장희직","롯데리아 vs 맘스터치","https://bimage.interpark.com/partner/goods_image/5/3/7/5/354285375h.jpg"),
        CommentData(2,2,1,"연수연","우하하","https://bimage.interpark.com/partner/goods_image/5/3/7/5/354285375h.jpg"),
        CommentData(3,1,3,"먀먀먀","키득키득","https://bimage.interpark.com/partner/goods_image/5/3/7/5/354285375h.jpg"))

    override fun addComment(params: AddCommentParams): Call<Unit> {
        TODO("Not yet implemented")
    }

    override fun editComment() {
        TODO("Not yet implemented")
    }

    override fun deleteComment(commentId: Int) {
        TODO("Not yet implemented")
    }

    override fun fetchComments(eventId: Int): List<CommentData> {
        return commentList.filter { it.eventId == eventId }
    }

    override fun getComment(commentId: Int): CommentData {
        return commentList.find { it.id == commentId }!!
    }
}