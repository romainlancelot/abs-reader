package com.absreader.networks.dto.login

data class Permissions(
    val accessAllLibraries: Boolean,
    val accessAllTags: Boolean,
    val accessExplicitContent: Boolean,
    val delete: Boolean,
    val download: Boolean,
    val update: Boolean,
    val upload: Boolean
)