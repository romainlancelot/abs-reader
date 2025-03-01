package com.absreader.data.network.dto.library_items_data

data class Metadata(
    val birthtimeMs: Long,
    val ctimeMs: Long,
    val ext: String,
    val filename: String,
    val mtimeMs: Long,
    val path: String,
    val relPath: String,
    val size: Int
)