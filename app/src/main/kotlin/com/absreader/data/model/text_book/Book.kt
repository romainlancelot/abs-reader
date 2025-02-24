package com.absreader.data.model.text_book

data class Book(
    val id: String,
    val title: String,
    val authorId: String,
    val coverId: String,
    val coverUrl: String,
    val createdAt: String,
    val author: Author? = null,
    val pages: List<Page>? = null
)
