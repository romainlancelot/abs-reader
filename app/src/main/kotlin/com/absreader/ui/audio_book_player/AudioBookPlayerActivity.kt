package com.absreader.ui.audio_book_player

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.absreader.R
import com.absreader.utils.HeaderManager
import com.squareup.picasso.Picasso
import java.util.concurrent.TimeUnit

class AudioBookPlayerActivity: AppCompatActivity() {
    private lateinit var playerView: PlayerView
    private lateinit var player: ExoPlayer
    private lateinit var seekBar: SeekBar
    private lateinit var currentTimeText: TextView
    private lateinit var totalTimeText: TextView
    private lateinit var playPauseButton: ImageButton
    private lateinit var rewindButton: ImageButton
    private lateinit var forwardButton: ImageButton
    private lateinit var bookTitleText: TextView
    private lateinit var bookCover: ImageView

    // Skip time in milliseconds (10 seconds)
    private val skipTime = 10000L

    private val viewModel: AudioBookPlayerViewModel = AudioBookPlayerViewModel()
    private val handler = Handler(Looper.getMainLooper())
    private val updateSeekBar = object : Runnable {
        override fun run() {
            val currentPosition = player.currentPosition
            seekBar.progress = currentPosition.toInt()
            currentTimeText.text = formatDuration(currentPosition)
            viewModel.updatePlaybackPosition(currentPosition)
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audio_book_player)

        val bookName = intent.getStringExtra("bookName").toString()
        HeaderManager(findViewById(R.id.header)).setup("\uD83C\uDFA7 Player")

        initializeViews()
        setupPlayer()
        setupListeners()
        bookTitleText.text = bookName

        val coverPath = intent.getStringExtra("coverPath")
        if (!coverPath.isNullOrEmpty()) {
            Picasso.get().load(coverPath).into(bookCover)
        } else {
            bookCover.setImageResource(R.drawable.generic_cover)
        }

        val absPath: String = intent.getStringExtra("absPath").toString()
        loadAudio(absPath)
    }

    private fun initializeViews() {
        playerView = findViewById(R.id.player_view)
        seekBar = findViewById(R.id.seek_bar)
        currentTimeText = findViewById(R.id.current_time)
        totalTimeText = findViewById(R.id.total_time)
        playPauseButton = findViewById(R.id.play_pause_button)
        rewindButton = findViewById(R.id.rewind_button)
        forwardButton = findViewById(R.id.forward_button)
        bookTitleText = findViewById(R.id.book_title)
        bookCover = findViewById(R.id.book_cover)
    }

    private fun setupPlayer() {
        player = ExoPlayer.Builder(this).build()
        playerView.player = player

        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) {
                    seekBar.max = player.duration.toInt()
                    totalTimeText.text = formatDuration(player.duration)
                    viewModel.updateDuration(player.duration)
                    handler.post(updateSeekBar)
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                viewModel.updatePlaybackState(isPlaying)
                updatePlayPauseButton(isPlaying)
                if (isPlaying) {
                    handler.post(updateSeekBar)
                } else {
                    handler.removeCallbacks(updateSeekBar)
                }
            }
        })
    }

    private fun setupListeners() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    player.seekTo(progress.toLong())
                    currentTimeText.text = formatDuration(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                handler.removeCallbacks(updateSeekBar)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                handler.post(updateSeekBar)
            }
        })

        playPauseButton.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        }

        rewindButton.setOnClickListener {
            val newPosition = (player.currentPosition - skipTime).coerceAtLeast(0)
            player.seekTo(newPosition)
        }

        forwardButton.setOnClickListener {
            val newPosition = (player.currentPosition + skipTime).coerceAtMost(player.duration)
            player.seekTo(newPosition)
        }
    }

    private fun loadAudio(path: String) {
        player.setMediaItem(MediaItem.fromUri(path))
        player.prepare()
        player.play()
    }

    private fun updatePlayPauseButton(isPlaying: Boolean) {
        playPauseButton.setImageResource(
            if (isPlaying) R.drawable.ic_pause
            else R.drawable.ic_play
        )
    }

    private fun formatDuration(duration: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) -
                TimeUnit.MINUTES.toSeconds(minutes)
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateSeekBar)
    }

    override fun onResume() {
        super.onResume()
        if (player.isPlaying) {
            handler.post(updateSeekBar)
        }
    }

    override fun onDestroy() {
        handler.removeCallbacks(updateSeekBar)
        player.release()
        super.onDestroy()
    }
}