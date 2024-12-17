package com.absreader.networks.services

import com.absreader.networks.dto.libraries.LibrariesDTO
import retrofit2.Call
import retrofit2.http.GET

interface HomeService {
    @GET("libraries")
    fun getLibrairies(): Call<LibrariesDTO>
}