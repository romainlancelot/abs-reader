package com.absreader.data.network.dto.audio_book_library_items

data class Result(
    val addedAt: Long,
    val birthtimeMs: Long,
    val collapsedSeries: CollapsedSeries,
    val ctimeMs: Long,
    val folderId: String,
    val id: String,
    val ino: String,
    val isFile: Boolean,
    val isInvalid: Boolean,
    val isMissing: Boolean,
    val libraryId: String,
    val media: Media,
    val mediaType: String,
    val mtimeMs: Long,
    val numFiles: Int,
    val path: String,
    val relPath: String,
    val size: Int,
    val updatedAt: Long
)