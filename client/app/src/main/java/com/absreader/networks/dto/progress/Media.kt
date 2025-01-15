package com.absreader.networks.dto.progress

data class Media(
    val autoDownloadEpisodes: Boolean,
    val autoDownloadSchedule: String,
    var coverPath: String?,
    val lastEpisodeCheck: Long,
    val maxEpisodesToKeep: Int,
    val maxNewEpisodesToDownload: Int,
    val metadata: Metadata,
    val numEpisodes: Int,
    val size: Int,
    val tags: List<Any>
)