package com.absreader.data.model.text_book

data class BookDetails(
    val id: String,
    val title: String,
    val authorName: String,
    val createdYear: String,
    val coverUrl: String,
    val pages: List<Page>? = null,
    val isTheReaderTheAuthor: Boolean
)
