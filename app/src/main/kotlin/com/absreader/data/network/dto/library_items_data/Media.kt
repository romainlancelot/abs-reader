package com.absreader.data.network.dto.library_items_data

data class Media(
    val audioFiles: List<Any?>,
    val chapters: List<Any?>,
    val coverPath: String,
    val duration: Float,
    val ebookFile: EbookFile,
    val id: String,
    val libraryItemId: String,
    val metadata: MetadataXX,
    val size: Int,
    val tags: List<Any?>,
    val tracks: List<Any?>
)