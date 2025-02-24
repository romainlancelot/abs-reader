package com.absreader.data.network.dto.text_book.book

import com.absreader.data.model.text_book.Book

data class FindManyBookmarkedBooksResponse(
    val books: List<Book>
)
