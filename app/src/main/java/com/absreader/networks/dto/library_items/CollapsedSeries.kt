package com.absreader.networks.dto.library_items

data class CollapsedSeries(
    val id: String,
    val name: String,
    val nameIgnorePrefix: String,
    val numBooks: Int
)