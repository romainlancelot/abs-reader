package com.absreader.ui.text_book_reading

import android.app.Application
import androidx.lifecycle.*
import com.absreader.data.model.text_book.Book
import com.absreader.data.model.text_book.BookDetails
import com.absreader.data.model.text_book.Page
import com.absreader.data.network.dto.text_book.book.FindUniqueBookResponse
import com.absreader.data.network.dto.text_book.bookmark.FindUniqueBookmarkResponse
import com.absreader.data.repository.TextBookBookRepository
import com.absreader.data.repository.TextBookBookmarkRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class TextBookReadingViewModel(
    application: Application,
    private val bookRepository: TextBookBookRepository,
    private val bookmarkRepository: TextBookBookmarkRepository
) : ViewModel() {

    private val _bookDetails = MutableLiveData<BookDetails>()
    val bookDetails: LiveData<BookDetails> get() = _bookDetails

    private val _currentPage = MutableLiveData<Page>()
    val currentPage: LiveData<Page> get() = _currentPage

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    var currentPageNumber: Int = 1
        private set

    fun loadInitialPage(bookId: String) {
        viewModelScope.launch {
            val response: Response<FindUniqueBookmarkResponse> = bookmarkRepository.findUnique(bookId)
            if (response.isSuccessful && response.body() != null) {
                currentPageNumber = response.body()!!.pageOrder.toInt()
            }
            updateCurrentPage()
        }
    }

    fun fetchBookDetails(bookId: String) {
        viewModelScope.launch {
            val response: Response<FindUniqueBookResponse> = bookRepository.findUnique(bookId)
            if (response.isSuccessful) {
                response.body()?.let { bodyResponse ->
                    val book: Book = bodyResponse.book
                    val year: String = if (book.createdAt.length >= 4) book.createdAt.substring(0, 4) else ""
                    val details = BookDetails(
                        id = book.id,
                        title = book.title,
                        authorName = book.author?.name ?: "Unknown",
                        createdYear = year,
                        coverUrl = book.coverUrl,
                        isTheReaderTheAuthor = bodyResponse.isTheReaderTheAuthor,
                        pages = book.pages
                    )
                    _bookDetails.value = details
                    updateCurrentPage()
                }
            } else {
                _errorMessage.value = "Load of the book data failed."
            }
        }
    }

    private fun updateCurrentPage() {
        _bookDetails.value?.pages?.let { pages ->
            if (pages.isNotEmpty() && currentPageNumber in 1..pages.size) {
                _currentPage.value = pages[currentPageNumber - 1]
            } else {
                _errorMessage.value = "Page not found."
            }
        }
    }

    private fun updateBookmark(bookId: String, currentPageId: String) {
        viewModelScope.launch {
            val response = bookmarkRepository.upsertBookmark(bookId, currentPageId)
            if (!response.isSuccessful) {
                _errorMessage.value = "Update of the bookmark failed."
            }
        }
    }

    fun nextPage(bookId: String) {
        _bookDetails.value?.pages?.let { pages ->
            if (currentPageNumber < pages.size) {
                currentPageNumber++
                updateCurrentPage()
                updateBookmark(bookId, _bookDetails.value!!.pages!!.get(currentPageNumber).id)
            } else {
                _errorMessage.value = "You are on the last page."
            }
        }
    }

    fun previousPage(bookId: String) {
        if (currentPageNumber > 1) {
            currentPageNumber--
            updateCurrentPage()
            updateBookmark(bookId, _bookDetails.value!!.pages!!.get(currentPageNumber).id)
        } else {
            _errorMessage.value = "You are on the first page."
        }
    }
}
