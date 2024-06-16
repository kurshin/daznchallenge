package com.kurshin.daznchallenge.presentation.ui.video

import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.ExoPlayer

class VideoViewModel: ViewModel() {

    internal var currentPosition = 0L
    internal var playWhenReady = true

    fun restorePlayerState(player: ExoPlayer?) {
        player?.let {
            it.playWhenReady = playWhenReady
            it.seekTo(currentPosition)
        }
    }

    fun savePlayerState(player: ExoPlayer?) {
        player?.let {
            currentPosition = it.currentPosition
            playWhenReady = it.playWhenReady
        }
    }
}