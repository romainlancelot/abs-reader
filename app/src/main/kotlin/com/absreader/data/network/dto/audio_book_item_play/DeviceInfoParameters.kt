package com.absreader.data.network.dto.audio_book_item_play

data class DeviceInfoParameters(
    val deviceId: String,
    val clientName: String,
    val clientVersion: String,
    val manufacturer: String,
    val model: String,
    val sdkVersion: Int
)