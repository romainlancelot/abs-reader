package com.absreader.data.network.dto.library_items_data

data class EbookFile(
    val addedAt: Long,
    val ebookFormat: String,
    val fileType: String,
    val ino: String,
    val isSupplementary: Any,
    val metadata: Metadata,
    val updatedAt: Long
)