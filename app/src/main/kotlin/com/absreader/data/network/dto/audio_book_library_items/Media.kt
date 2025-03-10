package com.absreader.data.network.dto.audio_book_library_items

data class Media(
    var coverPath: String?,
    val duration: Float,
    val ebookFileFormat: Any,
    val metadata: Metadata,
    val numAudioFiles: Int,
    val numChapters: Int,
    val numTracks: Int,
    val size: Int,
    val tags: List<Any>
)