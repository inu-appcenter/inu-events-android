package org.inu.events.data.repository.impl

import android.util.Log
import org.inu.events.data.httpservice.CommentHttpService
import org.inu.events.data.model.dto.AddCommentParams
import org.inu.events.data.model.dto.UpdateCommentParams
import org.inu.events.data.model.entity.Comment
import org.inu.events.data.repository.CommentRepository

class CommentRepositoryImpl(
    private val httpService: CommentHttpService
) : CommentRepository {

    override fun getComments(eventId: Int): List<Comment> {
        // todo - 나중에 .map 부분 지워야함
        return httpService.fetchComments(eventId).execute().body()!!
            .map { it.copy(wroteByMe = true) }
    }

    override fun postComment(params: AddCommentParams) {
        httpService.addComment(params).execute()
    }

    override fun updateComment(id: Int, params: UpdateCommentParams) {
        httpService.editComment(id, params).execute()
    }

    override fun deleteComment(commentId: Int) {
        httpService.deleteComment(commentId).execute()
    }
}