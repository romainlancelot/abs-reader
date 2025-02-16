package com.absreader.data.network.dto.audio_book_item_play

data class LibraryFile(
    val addedAt: Long,
    val fileType: String,
    val ino: String,
    val metadata: Metadata,
    val updatedAt: Long
)