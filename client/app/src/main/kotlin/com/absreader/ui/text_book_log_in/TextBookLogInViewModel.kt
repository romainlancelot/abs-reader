package com.absreader.ui.text_book_log_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.absreader.data.network.dto.auth.TextBookLogInRequest
import com.absreader.data.network.dto.auth.TextBookLogInResponse
import com.absreader.data.repository.TextBookAuthRepository
import kotlinx.coroutines.launch

class TextBookLogInViewModel(
    private val repository: TextBookAuthRepository
) : ViewModel() {
    private val _loginResult = MutableLiveData<Result<TextBookLogInResponse>>()
    val loginResult: LiveData<Result<TextBookLogInResponse>> = _loginResult

    fun logIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                val request = TextBookLogInRequest(email, password)
                val response = repository.logIn(request)
                _loginResult.value = Result.success(response)
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            }
        }
    }
}
