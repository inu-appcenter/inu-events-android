package org.inu.events.data.service

import org.inu.events.data.CommentData
import org.inu.events.data.model.dto.AddCommentParams
import retrofit2.Call
import retrofit2.http.*

interface CommentService {
    @POST("/comments")
    fun addComment(
        @Body params: AddCommentParams
    ): Call<Unit>

    @PUT("/comments/{commentId}")
    fun editComment(
        // todo
    )

    @DELETE("/comments/{commentId}")
    fun deleteComment(
        @Path("commentId") commentId: Int
        // todo - event id와 userid가 같을 때 삭제 ?
    )

    @GET("/comments")
    fun fetchComments(
        @Query("eventId") eventId: Int
    ):List<CommentData>

    @GET("/comments/{commentId}")
    fun getComment(
        @Path("commentId") commentId: Int
    ):CommentData

}