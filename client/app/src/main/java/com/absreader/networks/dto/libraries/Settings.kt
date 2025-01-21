package com.absreader.networks.dto.libraries

data class Settings(
    val autoScanCronExpression: Any,
    val coverAspectRatio: Int,
    val disableWatcher: Boolean,
    val skipMatchingMediaWithAsin: Boolean,
    val skipMatchingMediaWithIsbn: Boolean
)