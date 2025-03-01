package com.absreader.data.network.dto.library_items_data

data class MetadataXX(
    val abridged: Boolean,
    val asin: Any,
    val authorName: String,
    val authorNameLF: String,
    val authors: List<Author>,
    val description: Any,
    val descriptionPlain: Any,
    val explicit: Boolean,
    val genres: List<Any>,
    val isbn: Any,
    val language: Any,
    val narratorName: String,
    val narrators: List<Any>,
    val publishedDate: Any,
    val publishedYear: Any,
    val publisher: Any,
    val series: List<Any>,
    val seriesName: String,
    val subtitle: Any,
    val title: String,
    val titleIgnorePrefix: String
)