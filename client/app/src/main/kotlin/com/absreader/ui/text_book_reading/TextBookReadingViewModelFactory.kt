package com.absreader.ui.text_book_reading

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.absreader.data.repository.TextBookBookRepository
import com.absreader.data.repository.TextBookBookmarkRepository

class TextBookReadingViewModelFactory(
    private val application: Application,
    private val bookRepository: TextBookBookRepository,
    private val bookmarkRepository: TextBookBookmarkRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TextBookReadingViewModel::class.java))
            return TextBookReadingViewModel(
                application,
                bookRepository,
                bookmarkRepository
            ) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
