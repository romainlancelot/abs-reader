package com.absreader.networks.dto.libraries

data class Library(
    val createdAt: Long,
    val displayOrder: Int,
    val folders: List<Folder>,
    val icon: String,
    val id: String,
    val lastUpdate: Long,
    val mediaType: String,
    val name: String,
    val provider: String,
    val settings: Settings
)