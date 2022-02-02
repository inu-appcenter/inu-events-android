package org.inu.events.data.service

import org.inu.events.data.CommentData
import org.inu.events.data.model.Article
import org.inu.events.data.model.User
import retrofit2.Call

class DummyCommentService: CommentService {
    private var commentList: List<CommentData> = listOf(CommentData(0,0,"장희직","롯데리아 vs 맘스터치","https://bimage.interpark.com/partner/goods_image/5/3/7/5/354285375h.jpg"),
        CommentData(0,1,"연수연","우하하","https://bimage.interpark.com/partner/goods_image/5/3/7/5/354285375h.jpg"),
        CommentData(1,0,"먀먀먀","키득키득","https://bimage.interpark.com/partner/goods_image/5/3/7/5/354285375h.jpg"))

    override fun addComment(content: String): Call<Unit> {
        TODO("Not yet implemented")
    }

    override fun editComment() {
        TODO("Not yet implemented")
    }

    override fun deleteComment() {
        TODO("Not yet implemented")
    }

    override fun fetchComment(): List<CommentData>{
        return commentList
    }

    override fun getComment(userId: Int?, eventId: Int?): CommentData {
        if(eventId == null) return commentList[0]
        return commentList[eventId]
    }
}