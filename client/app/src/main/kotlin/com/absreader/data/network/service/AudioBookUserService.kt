package com.absreader.data.network.service

import com.absreader.data.network.dto.audio_book_progress.ProgressDTO
import retrofit2.Call
import retrofit2.http.GET

interface AudioBookUserService {
    @GET("/api/me/items-in-progress")
    fun getItemsInProgress(): Call<ProgressDTO>
}