package com.absreader.networks.services

import com.absreader.networks.dto.progress.ProgressDTO
import retrofit2.Call
import retrofit2.http.GET

interface UserService {
    @GET("/api/me/items-in-progress")
    fun getItemsInProgress(): Call<ProgressDTO>
}