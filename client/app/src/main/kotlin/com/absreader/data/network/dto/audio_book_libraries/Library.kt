package com.absreader.data.network.dto.audio_book_libraries

data class Library(
    val createdAt: Long,
    val displayOrder: Int,
    val folders: List<Folder>,
    val icon: String,
    val id: String,
    val lastUpdate: Long,
    val mediaType: String,
    var name: String,
    val provider: String,
    val settings: Settings
)