package com.absreader.data.repository

import android.app.Application
import com.absreader.data.network.RetrofitClient
import com.absreader.data.network.dto.text_book.book.CreateBookResponse
import com.absreader.data.network.dto.text_book.book.FindManyBookmarkedBooksResponse
import com.absreader.data.network.dto.text_book.book.FindManyBooksOfMineResponse
import com.absreader.data.network.dto.text_book.book.FindManyBooksResponse
import com.absreader.data.network.dto.text_book.book.FindUniqueBookResponse
import com.absreader.data.network.service.TextBookBookApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

class TextBookBookRepository(application: Application) {

    private val apiServiceWithoutAuth =
        RetrofitClient
            .getTextBookApiInstanceWithoutAuth(application)
            .create(TextBookBookApiService::class.java)

    private val apiServiceWithAuth =
        RetrofitClient
            .getTextBookApiInstanceWithAuth(application)
            .create(TextBookBookApiService::class.java)

    suspend fun createBook(
        title: String,
        file: File
    ): Response<CreateBookResponse> = withContext(Dispatchers.IO) {
        try {

            val mimeType = when (file.extension.lowercase()) {
                "pdf" -> "application/pdf"
                "png" -> "image/png"
                "jpg", "jpeg" -> "image/jpeg"
                else -> "application/octet-stream"
            }

            val requestFile = RequestBody.create(
                MediaType.parse(mimeType),
                file
            )

            val coverFilePart = MultipartBody.Part.createFormData(
                "coverFile",
                file.name,
                requestFile
            )

            apiServiceWithAuth.createBook(
                title = title,
                coverFile = coverFilePart
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    suspend fun findUnique(bookId: String): Response<FindUniqueBookResponse> {
        return this.apiServiceWithAuth.findUnique(bookId)
    }

    suspend fun findMany(): Response<FindManyBooksResponse> {
        return this.apiServiceWithoutAuth.findMany()
    }

    suspend fun findManyOfMine(): Response<FindManyBooksOfMineResponse> {
        return this.apiServiceWithAuth.findManyOfMine()
    }

    suspend fun findManyBookmarked(): Response<FindManyBookmarkedBooksResponse> {
        return this.apiServiceWithAuth.findBookmarkedBooks()
    }
}
