package org.inu.events.data.httpservice

import org.inu.events.data.model.entity.Comment
import org.inu.events.data.model.dto.AddCommentParams
import org.inu.events.data.model.dto.UpdateCommentParams
import retrofit2.Call
import retrofit2.http.*

interface CommentHttpService {
    @POST("/comments")
    fun addComment(
        @Body params: AddCommentParams
    ): Call<Unit>

    @PUT("/comments/{commentId}")
    fun editComment(
        @Body params: UpdateCommentParams
    ): Call<Unit>

    @DELETE("/comments/{commentId}")
    fun deleteComment(
        @Path("commentId") commentId: Int
        // todo - event id와 userid가 같을 때 삭제 ?
    ): Call<Unit>

    @GET("/comments")
    fun fetchComments(
        @Query("eventId") eventId: Int
    ): Call<List<Comment>>

    @GET("/comments/{commentId}")
    fun getComment(
        @Path("commentId") commentId: Int
    ): Call<Comment>

}