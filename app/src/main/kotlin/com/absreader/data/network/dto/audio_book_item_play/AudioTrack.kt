package com.absreader.data.network.dto.audio_book_item_play

data class AudioTrack(
    val contentUrl: String,
    val duration: Double,
    val index: Int,
    val metadata: Metadata,
    val mimeType: String,
    val startOffset: Int,
    val title: String
)