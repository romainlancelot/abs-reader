package com.absreader.data.network.service

import com.absreader.data.network.dto.auth.TextBookLogInRequest
import com.absreader.data.network.dto.auth.TextBookLogInResponse
import com.absreader.data.network.dto.auth.TextBookSignUpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

public interface TextBookAuthApiService {
    @POST("log-in")
    public suspend fun logIn(
        @Body request: TextBookLogInRequest
    ): Response<TextBookLogInResponse>

    @POST("sign-up")
    public suspend fun signUp(
        @Body request: TextBookSignUpRequest
    ): Response<Unit>
}
