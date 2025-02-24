package com.absreader.data.network.dto.audio_book_library_items

data class CollapsedSeries(
    val id: String,
    val name: String,
    val nameIgnorePrefix: String,
    val numBooks: Int
)