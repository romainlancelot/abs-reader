package com.absreader.data.repository

import android.app.Application
import com.absreader.data.network.RetrofitClient
import com.absreader.data.network.dto.text_book.bookmark.FindUniqueBookmarkResponse
import com.absreader.data.network.dto.text_book.bookmark.UpsertBookmarkRequest
import com.absreader.data.network.dto.text_book.bookmark.UpsertBookmarkResponse
import com.absreader.data.network.service.TextBookBookmarkApiService
import retrofit2.Response

class TextBookBookmarkRepository(application: Application) {

    private val apiServiceWithAuth =
        RetrofitClient
            .getTextBookApiInstanceWithAuth(application)
            .create(TextBookBookmarkApiService::class.java)

    suspend fun upsertBookmark(
        bookId: String,
        currentPageId: String
    ): Response<UpsertBookmarkResponse> {
        val request: UpsertBookmarkRequest = UpsertBookmarkRequest(currentPageId)
        return this.apiServiceWithAuth.upsert(bookId, request);
    }

    suspend fun findUnique(
        bookId: String
    ): Response<FindUniqueBookmarkResponse> {
        return this.apiServiceWithAuth.findUnique(bookId)
    }
}
