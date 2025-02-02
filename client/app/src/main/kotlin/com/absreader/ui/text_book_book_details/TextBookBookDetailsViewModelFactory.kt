package com.absreader.ui.text_book_book_details

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.absreader.data.repository.TextBookBookRepository
import com.absreader.ui.text_book_home.TextBookHomeViewModel

class TextBookBookDetailsViewModelFactory(
    private val application: Application,
    private val repository: TextBookBookRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TextBookBookDetailsViewModel::class.java))
            return TextBookBookDetailsViewModel(application, repository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
