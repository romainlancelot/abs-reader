package com.absreader.data.network.dto.audio_book_item_play

data class Episode(
    val addedAt: Long,
    val audioFile: AudioFile,
    val audioTrack: AudioTrack,
    val description: String,
    val duration: Double,
    val enclosure: Enclosure,
    val episode: String,
    val episodeType: String,
    val id: String,
    val index: Int,
    val libraryItemId: String,
    val pubDate: String,
    val publishedAt: Long,
    val season: String,
    val size: Int,
    val subtitle: String,
    val title: String,
    val updatedAt: Long
)