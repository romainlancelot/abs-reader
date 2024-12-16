package com.absreader.networks.dto.login

data class User(
    val bookmarks: List<Any>,
    val createdAt: Long,
    val id: String,
    val isActive: Boolean,
    val isLocked: Boolean,
    val itemTagsAccessible: List<Any>,
    val lastSeen: Long,
    val librariesAccessible: List<Any>,
    val mediaProgress: List<MediaProgress>,
    val permissions: Permissions,
    val seriesHideFromContinueListening: List<Any>,
    val token: String,
    val type: String,
    val username: String
)