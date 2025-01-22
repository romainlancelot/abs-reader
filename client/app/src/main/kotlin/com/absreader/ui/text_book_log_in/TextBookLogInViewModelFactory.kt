package com.absreader.ui.text_book_log_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.absreader.data.repository.TextBookAuthRepository

class TextBookLogInViewModelFactory(
    private val repository: TextBookAuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TextBookLogInViewModel::class.java))
            return TextBookLogInViewModel(repository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
