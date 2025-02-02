package com.absreader.data.network.service

import com.absreader.data.network.dto.text_book.book.CreateBookResponse
import com.absreader.data.network.dto.text_book.book.FindManyBookmarkedBooksResponse
import com.absreader.data.network.dto.text_book.book.FindManyBooksOfMineResponse
import com.absreader.data.network.dto.text_book.book.FindUniqueBookResponse
import com.absreader.data.network.dto.text_book.book.FindManyBooksResponse
import com.absreader.data.network.dto.text_book.book.UpdateBookInformationRequest
import com.absreader.data.network.dto.text_book.book.UpdateBookInformationResponse
import com.absreader.data.network.dto.text_book.book.UpdateContentResponse
import com.absreader.data.network.dto.text_book.book.UpdateCoverResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part

interface TextBookBookApiService {
    /**
     * Create a book.
     */
    @Multipart
    @POST("/books")
    suspend fun createBook(
        @Part("title") title: String,
        @Part coverFile: MultipartBody.Part?
    ): Response<CreateBookResponse>

    /**
     * Find a unique book.
     */
    @GET("/books/{id}")
    public suspend fun findUnique(): Response<FindUniqueBookResponse>

    /**
     * Find many books.
     */
    @GET("/books")
    public suspend fun findMany(): Response<FindManyBooksResponse>

    /**
     * Find many books of mine.
     */
    @GET("/books/mine")
    public suspend fun findManyOfMine(): Response<FindManyBooksOfMineResponse>

    /**
     * Find books I have a bookmark on.
     */
    @GET("/books/bookmarks")
    public suspend fun findBookmarkedBooks(): Response<FindManyBookmarkedBooksResponse>

    /**
     * Update book information.
     */
    @PATCH("/books/{id}/information")
    public suspend fun updateInformation(
        @Body request: UpdateBookInformationRequest
    ): Response<UpdateBookInformationResponse>

    /**
     * Update book cover.
     */
    @PATCH("/books/{id}/cover")
    public suspend fun updateCover(
    ): Response<UpdateCoverResponse>

    /**
     * Update book content.
     */
    @PATCH("/books/{id}/content")
    public suspend fun updateContent(
    ): Response<UpdateContentResponse>

    /**
     * Delete a book.
     */
    @DELETE("/books/{id}")
    public suspend fun delete(
    ): Response<Unit>
}
