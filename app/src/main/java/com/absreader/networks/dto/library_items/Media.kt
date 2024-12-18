package com.absreader.networks.dto.library_items

data class Media(
    var coverPath: String,
    val duration: Double,
    val ebookFileFormat: Any,
    val metadata: Metadata,
    val numAudioFiles: Int,
    val numChapters: Int,
    val numTracks: Int,
    val size: Int,
    val tags: List<Any>
)