package com.absreader.ui.text_book_log_in

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.absreader.data.network.dto.auth.TextBookLogInRequest
import com.absreader.data.network.dto.auth.TextBookLogInResponse
import com.absreader.data.repository.TextBookAuthRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class TextBookLogInViewModel(application: Application, private val repository: TextBookAuthRepository) : AndroidViewModel(application) {

    private val _hasLogInSucceeded = MutableLiveData<Boolean>()
    val hasLogInSucceeded: LiveData<Boolean> = _hasLogInSucceeded

    private val _logInMessage = MutableLiveData<String>()
    val logInMessage: LiveData<String> = _logInMessage

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val request: TextBookLogInRequest = TextBookLogInRequest(email, password)
                val response: Response<TextBookLogInResponse> = repository.logIn(request)
                _hasLogInSucceeded.value = response.isSuccessful
                if (response.isSuccessful) {
                    val sharedPreferences: SharedPreferences = getApplication<Application>().getSharedPreferences("auth", Context.MODE_PRIVATE)
                    val jwt: String = response.body()?.jwt ?: ""
                    sharedPreferences.edit().putString("text_book_api_jwt", jwt).apply()
                    _logInMessage.value = "Login successful"
                    _hasLogInSucceeded.value = true
                } else {
                    _logInMessage.value = response.message()
                    _hasLogInSucceeded.value = false
                }
            } catch (e: Exception) {
                _hasLogInSucceeded.value = e.message != null
            }
        }
    }

}
