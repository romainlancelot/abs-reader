package com.absreader.networks.dto.progress

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
    val libraryId: String,
    val media: Media,
    val mediaType: String,
    val mtimeMs: Long,
    val numFiles: Int,
    val path: String,
    val progressLastUpdate: Long,
    val recentEpisode: RecentEpisode,
    val relPath: String,
    val size: Int,
    val updatedAt: Long
)