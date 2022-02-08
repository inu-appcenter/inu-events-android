package org.inu.events.data.repository

import org.inu.events.data.model.dto.AddCommentParams
import org.inu.events.data.model.dto.UpdateCommentParams
import org.inu.events.data.model.entity.Comment

interface CommentRepository {
    fun getComments(eventId: Int): List<Comment>
    fun postComment(params: AddCommentParams)
    fun updateComment(id: Int, params: UpdateCommentParams)
    fun deleteComment(commentId: Int)
}