package org.inu.events.data.service

import org.inu.events.data.CommentData
import retrofit2.Call
import retrofit2.http.*

interface CommentService {
    @POST("/comments")
    fun addComment(
        @Body content: String
    ): Call<Unit>

    @PUT("/comments/{commentId}")
    fun editComment(
        // todo
    )

    @DELETE("/comments/{commentId}")
    fun deleteComment(

    )

    @GET("/comments/")
    fun fetchComment(

    ):List<CommentData>

    @GET("/comments/{commentId}")
    fun getComment(
        @Path("userId") userId: Int?,
        @Path("eventId") eventId: Int?
    ):CommentData

}