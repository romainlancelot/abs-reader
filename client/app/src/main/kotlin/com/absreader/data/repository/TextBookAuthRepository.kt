package com.absreader.data.repository;

import android.app.Application
import com.absreader.data.network.RetrofitClient
import com.absreader.data.network.dto.auth.TextBookLogInRequest
import com.absreader.data.network.dto.auth.TextBookLogInResponse;
import com.absreader.data.network.service.TextBookAuthApiService;
import retrofit2.Response

class TextBookAuthRepository(application: Application) {

    private val apiServiceWithoutAuth =
        RetrofitClient
            .getTextBookApiInstanceWithoutAuth(application)
            .create(TextBookAuthApiService::class.java)

    suspend fun logIn(request: TextBookLogInRequest): Response<TextBookLogInResponse> {
        return apiServiceWithoutAuth.logIn(request)
    }

}
