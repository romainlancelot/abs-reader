package com.absreader.networks.services

import com.absreader.networks.dto.libraries.LibrariesDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface HomeService {
    @GET("libraries")
    fun getLibrairies(@Header("Authorization") token: String): Call<LibrariesDTO>
}