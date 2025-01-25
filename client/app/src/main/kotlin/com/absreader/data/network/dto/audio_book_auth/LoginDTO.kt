package com.absreader.data.network.dto.audio_book_auth

data class LoginDTO(
    val serverSettings: ServerSettings,
    val user: User,
    val userDefaultLibraryId: String,
    val Source: String
)