package com.absreader.ui.audio_book_player

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.ui.PlayerView
import com.absreader.R
import com.absreader.ui.base.BaseActivity
import com.absreader.utils.HeaderManager

class AudioBookPlayerActivity: BaseActivity() {
    private lateinit var playerView: PlayerView
    private lateinit var player: ExoPlayer
    private val viewModel: AudioBookPlayerViewModel = AudioBookPlayerViewModel()

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container = findViewById<FrameLayout>(R.id.container)
        layoutInflater.inflate(R.layout.activity_audio_book_player, container, true)

        HeaderManager(findViewById(R.id.header)).setup(
            intent.getStringExtra("bookName").toString()
        )

        playerView = findViewById(R.id.player_view)

        val bookId = intent.getStringExtra("itemId").toString()
        viewModel.fetchBook(this, bookId)
        val dataSourceFactory = DefaultHttpDataSource.Factory().setDefaultRequestProperties(
            mapOf("Authorization" to "${getSharedPreferences("auth", MODE_PRIVATE).getString("audio_book_api_jwt", "")}")
        )
        viewModel.audioFiles.observe(this) { audioFiles ->
            if (audioFiles.isNotEmpty()) {
                player = ExoPlayer.Builder(this).build()
                playerView.player = player
                val m3u8Url = audioFiles.map { audioFile ->
                    "https://abs.romsko.fr${audioFile.contentUrl}"
                }
                val mediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(
                    MediaItem.fromUri(m3u8Url[0])
                )
                player.setMediaSource(mediaSource)
                player.prepare()
                player.play()
            } else {
                Toast.makeText(this, "No audio files found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}