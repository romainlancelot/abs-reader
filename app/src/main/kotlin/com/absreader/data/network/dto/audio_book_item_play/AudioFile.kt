package com.absreader.data.network.dto.audio_book_item_play

data class AudioFile(
    val addedAt: Long,
    val bitRate: Int,
    val channelLayout: String,
    val channels: Int,
    val chapters: List<Any?>,
    val codec: String,
    val discNumFromFilename: Any,
    val discNumFromMeta: Any,
    val duration: Double,
    val embeddedCoverArt: String,
    val error: Any,
    val exclude: Boolean,
    val format: String,
    val index: Int,
    val ino: String,
    val language: Any,
    val manuallyVerified: Boolean,
    val metaTags: MetaTags,
    val metadata: Metadata,
    val mimeType: String,
    val timeBase: String,
    val trackNumFromFilename: Any,
    val trackNumFromMeta: Any,
    val updatedAt: Long
)