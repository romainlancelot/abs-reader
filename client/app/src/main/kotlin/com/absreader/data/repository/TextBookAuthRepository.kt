package com.absreader.data.repository;

import com.absreader.data.network.dto.auth.TextBookLogInRequest;
import com.absreader.data.network.dto.auth.TextBookLogInResponse;
import com.absreader.data.network.service.TextBookAuthApiService;

public class TextBookAuthRepository(
    private val apiService: TextBookAuthApiService
) {
    suspend fun logIn(request: TextBookLogInRequest): TextBookLogInResponse {
        return apiService.logIn(request);
    }
}
