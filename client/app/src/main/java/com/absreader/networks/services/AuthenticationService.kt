package com.absreader.networks.services

import com.absreader.networks.dto.login.LoginDTO
import com.absreader.networks.models.LoginParameters
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationService {
    @POST("/login")
    fun login(@Body loginParameters: LoginParameters): Call<LoginDTO>
}