package com.absreader.data.repository;

import android.app.Application
import com.absreader.data.network.RetrofitClient
import com.absreader.data.network.dto.auth.TextBookLogInRequest
import com.absreader.data.network.dto.auth.TextBookLogInResponse;
import com.absreader.data.network.service.TextBookAuthApiService;
import retrofit2.Response

class TextBookAuthRepository(
    application: Application
) {

    private val apiService = RetrofitClient
        .getTextBookApiInstanceWithAuth(application)
        .create(TextBookAuthApiService::class.java)

    suspend fun logIn(
        email: String,
        password: String
    ): Response<TextBookLogInResponse> {
        val request = TextBookLogInRequest(email, password)
        return apiService.logIn(request)
    }

}
