package org.inu.events.data.repository.mock

import org.inu.events.data.model.dto.AddCommentParams
import org.inu.events.data.model.dto.UpdateCommentParams
import org.inu.events.data.model.entity.Comment
import org.inu.events.data.repository.CommentRepository

class CommentRepositoryMock : CommentRepository {
    private val c = mutableListOf(
        Comment(
            1,
            3,
            3,
            "장희직",
            "롯데리아 vs 맘스터치",
            "https://bimage.interpark.com/partner/goods_image/5/3/7/5/354285375h.jpg"
        ),
        Comment(
            2,
            2,
            1,
            "연수연",
            "우하하",
            "https://bimage.interpark.com/partner/goods_image/5/3/7/5/354285375h.jpg"
        ),
        Comment(
            3,
            1,
            3,
            "먀먀먀",
            "키득키득",
            "https://bimage.interpark.com/partner/goods_image/5/3/7/5/354285375h.jpg"
        )
    )

    override fun getComments(eventId: Int): List<Comment> {
        return c.filter { it.eventId == eventId }
    }

    override fun postComment(params: AddCommentParams) {
        c.add(
            Comment(
                id = c.maxOf { it.id },
                userId = 1,
                eventId = params.eventId,
                nickName = "테스터",
                content = params.content,
                profile = "https://bimage.interpark.com/partner/goods_image/5/3/7/5/354285375h.jpg"
            )
        )
    }

    override fun updateComment(id: Int, params: UpdateCommentParams) {
        val found = c.find { it.id == id } ?: return

        c.remove(found)

        c.add(
            Comment(
                id = found.id,
                userId = found.userId,
                eventId = found.eventId,
                nickName = found.nickName,
                content = params.content,
                profile = found.profile
            )
        )
    }

    override fun deleteComment(commentId: Int) {
        c.removeIf { it.id == commentId }
    }
}