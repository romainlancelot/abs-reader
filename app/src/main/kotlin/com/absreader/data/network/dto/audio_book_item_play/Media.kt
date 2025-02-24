package com.absreader.data.network.dto.audio_book_item_play

data class Media(
    val autoDownloadEpisodes: Boolean,
    val autoDownloadSchedule: String,
    val coverPath: String,
    val episodes: List<Episode>,
    val lastEpisodeCheck: Long,
    val libraryItemId: String,
    val maxEpisodesToKeep: Int,
    val maxNewEpisodesToDownload: Int,
    val metadata: MetadataXXXX,
    val size: Int,
    val tags: List<Any?>
)