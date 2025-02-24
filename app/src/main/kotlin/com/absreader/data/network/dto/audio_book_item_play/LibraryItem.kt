package com.absreader.data.network.dto.audio_book_item_play

data class LibraryItem(
    val addedAt: Long,
    val birthtimeMs: Long,
    val ctimeMs: Long,
    val folderId: String,
    val id: String,
    val ino: String,
    val isFile: Boolean,
    val isInvalid: Boolean,
    val isMissing: Boolean,
    val lastScan: Long,
    val libraryFiles: List<LibraryFile>,
    val libraryId: String,
    val media: Media,
    val mediaType: String,
    val mtimeMs: Long,
    val path: String,
    val relPath: String,
    val scanVersion: String,
    val size: Int,
    val updatedAt: Long
)