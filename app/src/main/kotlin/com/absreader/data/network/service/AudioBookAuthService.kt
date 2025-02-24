package com.absreader.data.network.service

import com.absreader.data.network.dto.audio_book_auth.LoginDTO
import com.absreader.data.model.auth.AudioBookAuthLogin
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AudioBookAuthService {
    @POST("/login")
    fun login(@Body audioBookAuthLogin: AudioBookAuthLogin): Call<LoginDTO>
}