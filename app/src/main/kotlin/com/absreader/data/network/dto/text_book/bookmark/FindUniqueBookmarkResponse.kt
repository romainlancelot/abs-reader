package com.absreader.data.network.dto.text_book.bookmark

import com.absreader.data.model.text_book.Bookmark

data class FindUniqueBookmarkResponse(
    val bookmark: Bookmark,
    val pageOrder: Number
)
