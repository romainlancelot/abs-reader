package com.absreader.data.network.dto.audio_book_auth

data class MediaProgress(
    val currentTime: Double,
    val duration: Double,
    val episodeId: String,
    val finishedAt: Any,
    val hideFromContinueListening: Boolean,
    val id: String,
    val isFinished: Boolean,
    val lastUpdate: Long,
    val libraryItemId: String,
    val progress: Double,
    val startedAt: Long
)