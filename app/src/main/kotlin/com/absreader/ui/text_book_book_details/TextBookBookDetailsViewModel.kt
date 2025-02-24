package com.absreader.ui.text_book_book_details

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.absreader.data.model.text_book.Book
import com.absreader.data.model.text_book.BookDetails
import com.absreader.data.network.dto.text_book.book.FindUniqueBookResponse
import com.absreader.data.repository.TextBookBookRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class TextBookBookDetailsViewModel(
    application: Application,
    private val repository: TextBookBookRepository
) : ViewModel() {

    private val _bookDetails: MutableLiveData<BookDetails> = MutableLiveData<BookDetails>()
    val bookDetails: LiveData<BookDetails> get() = this._bookDetails

    fun fetchBookDetails(bookId: String) {
        viewModelScope.launch {
            val response: Response<FindUniqueBookResponse> = repository.findUnique(bookId)
            if (response.isSuccessful) {
                response.body()?.let { bodyResponse ->
                    val book: Book = bodyResponse.book
                    val year: String = if (book.createdAt.length >= 4) book.createdAt.substring(0, 4) else ""
                    val details: BookDetails = BookDetails(
                        id = book.id,
                        title = book.title,
                        authorName = book.author?.name ?: "Unknown",
                        createdYear = year,
                        coverUrl = book.coverUrl,
                        isTheReaderTheAuthor = bodyResponse.isTheReaderTheAuthor,
                    )
                    _bookDetails.value = details
                }
            } else {
            }
        }
    }
}
