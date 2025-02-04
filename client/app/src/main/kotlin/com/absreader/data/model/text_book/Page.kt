package com.absreader.data.model.text_book

data class Page(
    val id: String,
    val bookId: String,
    val order: Int,
    val fileId: String,
    val fileUrl: String
)
