package com.absreader.data.network.dto.text_book.book

import com.absreader.data.model.text_book.Book

data class FindManyBooksResponse(
    val books: List<Book>
)
