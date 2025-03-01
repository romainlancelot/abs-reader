package com.absreader.data.network.dto.library_items_data

data class LibraryFile(
    val addedAt: Long,
    val fileType: String,
    val ino: String,
    val isSupplementary: Boolean,
    val metadata: Metadata,
    val updatedAt: Long
)