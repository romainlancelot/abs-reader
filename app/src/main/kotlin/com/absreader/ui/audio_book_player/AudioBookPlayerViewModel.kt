package com.absreader.ui.audio_book_player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AudioBookPlayerViewModel : ViewModel() {
    private val _isPlaying = MutableLiveData<Boolean>()
    private val _currentPosition = MutableLiveData<Long>()
    private val _duration = MutableLiveData<Long>()
    val duration: LiveData<Long> = _duration

    fun updatePlaybackState(isPlaying: Boolean) {
        _isPlaying.value = isPlaying
    }

    fun updatePlaybackPosition(position: Long) {
        _currentPosition.value = position
    }

    fun updateDuration(duration: Long) {
        _duration.value = duration
    }
}