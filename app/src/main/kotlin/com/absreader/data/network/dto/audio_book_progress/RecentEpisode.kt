package com.absreader.data.network.dto.audio_book_progress

data class RecentEpisode(
    val addedAt: Long,
    val audioFile: AudioFile,
    val description: String,
    val enclosure: Enclosure,
    val episode: String,
    val episodeType: String,
    val id: String,
    val index: Int,
    val libraryItemId: String,
    val pubDate: String,
    val publishedAt: Long,
    val season: String,
    val subtitle: String,
    val title: String,
    val updatedAt: Long
)