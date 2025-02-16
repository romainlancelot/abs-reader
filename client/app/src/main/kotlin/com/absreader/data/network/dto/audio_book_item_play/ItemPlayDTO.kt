package com.absreader.data.network.dto.audio_book_item_play

data class ItemPlayDTO(
    val audioTracks: List<AudioTrack>,
    val chapters: List<Any?>,
    val coverPath: String,
    val currentTime: Int,
    val date: String,
    val dayOfWeek: String,
    val deviceInfo: DeviceInfo,
    val displayAuthor: String,
    val displayTitle: String,
    val duration: Double,
    val episodeId: String,
    val id: String,
    val libraryId: String,
    val libraryItem: LibraryItem,
    val libraryItemId: String,
    val mediaMetadata: MediaMetadata,
    val mediaPlayer: String,
    val mediaType: String,
    val playMethod: Int,
    val startTime: Int,
    val startedAt: Long,
    val timeListening: Int,
    val updatedAt: Long,
    val userId: String,
    val videoTrack: Any
)