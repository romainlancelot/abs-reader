package com.absreader.networks.dto

data class LoginDTO(
    val serverSettings: ServerSettings,
    val user: User,
    val userDefaultLibraryId: String,
    val Source: String
)