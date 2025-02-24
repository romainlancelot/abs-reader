package com.absreader.data.network.service

import com.absreader.data.network.dto.text_book.bookmark.FindUniqueBookmarkResponse
import com.absreader.data.network.dto.text_book.bookmark.UpsertBookmarkRequest
import com.absreader.data.network.dto.text_book.bookmark.UpsertBookmarkResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface TextBookBookmarkApiService {

    @PUT("/bookmarks/books/{bookId}")
    suspend fun upsert(
        @Path("bookId") bookId: String,
        @Body() request: UpsertBookmarkRequest
    ): Response<UpsertBookmarkResponse>

    @GET("bookmarks/books/{bookId}")
    suspend fun findUnique(
        @Path("bookId") bookId: String
    ): Response<FindUniqueBookmarkResponse>

}
