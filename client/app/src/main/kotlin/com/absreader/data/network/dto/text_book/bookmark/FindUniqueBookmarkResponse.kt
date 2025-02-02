package com.absreader.data.network.dto.text_book.bookmark

data class FindUniqueBookmarkResponse(
    val userId: String,
    val bookId: String,
    val currentPageId: String
)
