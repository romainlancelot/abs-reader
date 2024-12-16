package com.absreader.networks.dto.login

data class LoginDTO(
    val serverSettings: ServerSettings,
    val user: User,
    val userDefaultLibraryId: String,
    val Source: String
)