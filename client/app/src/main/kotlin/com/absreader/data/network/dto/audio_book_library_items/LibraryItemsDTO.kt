package com.absreader.data.network.dto.audio_book_library_items

data class LibraryItemsDTO(
    val collapseseries: Boolean,
    val filterBy: String,
    val include: String,
    val limit: Int,
    val mediaType: String,
    val minified: Boolean,
    val page: Int,
    val results: List<Result>,
    val sortBy: String,
    val sortDesc: Boolean,
    val total: Int
)