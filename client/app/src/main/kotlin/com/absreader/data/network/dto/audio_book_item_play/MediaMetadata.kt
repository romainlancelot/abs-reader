package com.absreader.data.network.dto.audio_book_item_play

data class MediaMetadata(
    val author: String,
    val description: String,
    val explicit: Boolean,
    val feedUrl: String,
    val genres: List<String>,
    val imageUrl: String,
    val itunesArtistId: Int,
    val itunesId: Int,
    val itunesPageUrl: String,
    val language: Any,
    val releaseDate: String,
    val title: String
)