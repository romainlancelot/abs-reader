package com.absreader.ui.text_book_home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.absreader.data.model.text_book.Book
import com.absreader.data.network.dto.text_book.book.FindManyBookmarkedBooksResponse
import com.absreader.data.network.dto.text_book.book.FindManyBooksOfMineResponse
import com.absreader.data.network.dto.text_book.book.FindManyBooksResponse
import com.absreader.data.repository.TextBookBookRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class TextBookHomeViewModel(
    application: Application,
    private val repository: TextBookBookRepository
) : ViewModel() {

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> get() = _books
    private val _areBooksLoading = MutableLiveData<Boolean>()
    val areBooksLoading: LiveData<Boolean> get() = _areBooksLoading

    private val _myBooks = MutableLiveData<List<Book>>()
    val myBooks: LiveData<List<Book>> get() = _myBooks
    private val _areMyBooksLoading = MutableLiveData<Boolean>()
    val areMyBooksLoading: LiveData<Boolean> get() = _areMyBooksLoading

    private val _bookmarkedBooks = MutableLiveData<List<Book>>()
    val bookmarkedBooks: LiveData<List<Book>> get() = _bookmarkedBooks
    private val _areBookmarkedBooksLoading = MutableLiveData<Boolean>()
    val areBookmarkedBooksLoading: LiveData<Boolean> get() = _areBookmarkedBooksLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchAllBooks() {
        viewModelScope.launch {
            try {
                fetchBooks()
                fetchMyBooks()
                fetchBookmarkedBooks()
            } catch (e: Exception) {
                _areBooksLoading.value = false
                _errorMessage.value = e.message
            }
        }
    }

    suspend fun fetchBooks(): Unit {
        val response: Response<FindManyBooksResponse> = repository.findMany()
        _areBooksLoading.value = response.isSuccessful
        if (response.isSuccessful) {
            _books.value = response.body()?.books
        } else {
            _errorMessage.value = response.message()
        }
    }

    suspend fun fetchMyBooks(): Unit {
        val response: Response<FindManyBooksOfMineResponse> = repository.findManyOfMine()
        _areMyBooksLoading.value = response.isSuccessful
        if (response.isSuccessful) {
            _myBooks.value = response.body()?.books
        } else {
            _errorMessage.value = response.message()
        }
    }

    suspend fun fetchBookmarkedBooks(): Unit {
        val response: Response<FindManyBookmarkedBooksResponse> = repository.findManyBookmarked()
        _areBookmarkedBooksLoading.value = response.isSuccessful
        if (response.isSuccessful) {
            _bookmarkedBooks.value = response.body()?.books
        } else {
            _errorMessage.value = response.message()
        }
    }
}
