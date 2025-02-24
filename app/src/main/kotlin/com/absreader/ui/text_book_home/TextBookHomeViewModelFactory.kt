package com.absreader.ui.text_book_home

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.absreader.data.repository.TextBookBookRepository

class TextBookHomeViewModelFactory(
    private val application: Application,
    private val repository: TextBookBookRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TextBookHomeViewModel::class.java))
            return TextBookHomeViewModel(application, repository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
