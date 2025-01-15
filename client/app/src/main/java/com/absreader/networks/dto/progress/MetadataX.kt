package com.absreader.networks.dto.progress

data class MetadataX(
    val birthtimeMs: Long,
    val ctimeMs: Long,
    val ext: String,
    val filename: String,
    val mtimeMs: Long,
    val path: String,
    val relPath: String,
    val size: Int
)