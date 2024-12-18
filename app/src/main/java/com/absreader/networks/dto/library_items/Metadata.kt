package com.absreader.networks.dto.library_items

data class Metadata(
    val asin: String,
    val authorName: String,
    val description: String,
    val explicit: Boolean,
    val genres: List<String>,
    val isbn: Any,
    val language: Any,
    val narratorName: String,
    val publishedDate: Any,
    val publishedYear: String,
    val publisher: String,
    val seriesName: String,
    val subtitle: Any,
    val title: String,
    val titleIgnorePrefix: String
)