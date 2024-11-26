package com.absreader.networks.dto

data class ServerSettings(
    val backupSchedule: String,
    val backupsToKeep: Int,
    val bookshelfView: Int,
    val chromecastEnabled: Boolean,
    val dateFormat: String,
    val homeBookshelfView: Int,
    val id: String,
    val language: String,
    val logLevel: Int,
    val loggerDailyLogsToKeep: Int,
    val loggerScannerLogsToKeep: Int,
    val maxBackupSize: Int,
    val metadataFileFormat: String,
    val rateLimitLoginRequests: Int,
    val rateLimitLoginWindow: Int,
    val scannerCoverProvider: String,
    val scannerDisableWatcher: Boolean,
    val scannerFindCovers: Boolean,
    val scannerParseSubtitle: Boolean,
    val scannerPreferMatchedMetadata: Boolean,
    val sortingIgnorePrefix: Boolean,
    val sortingPrefixes: List<String>,
    val storeCoverWithItem: Boolean,
    val storeMetadataWithItem: Boolean,
    val version: String
)