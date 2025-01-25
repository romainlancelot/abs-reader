package com.absreader.data.network.dto.audio_book_libraries

data class Settings(
    val autoScanCronExpression: Any,
    val coverAspectRatio: Int,
    val disableWatcher: Boolean,
    val skipMatchingMediaWithAsin: Boolean,
    val skipMatchingMediaWithIsbn: Boolean
)