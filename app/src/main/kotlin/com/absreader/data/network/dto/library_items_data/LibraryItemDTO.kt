package com.absreader.data.network.dto.library_items_data

data class LibraryItemDTO(
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
    val mediaItemShare: Any,
    val mediaType: String,
    val mtimeMs: Long,
    val oldLibraryItemId: Any,
    val path: String,
    val relPath: String,
    val rssFeed: Any,
    val scanVersion: String,
    val size: Int,
    val updatedAt: Long
)