package com.absreader.data.network.dto.audio_book_auth

data class Permissions(
    val accessAllLibraries: Boolean,
    val accessAllTags: Boolean,
    val accessExplicitContent: Boolean,
    val delete: Boolean,
    val download: Boolean,
    val update: Boolean,
    val upload: Boolean
)