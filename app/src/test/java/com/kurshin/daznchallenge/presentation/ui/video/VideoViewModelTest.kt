package com.kurshin.daznchallenge.presentation.ui.video

import com.google.android.exoplayer2.ExoPlayer
import io.mockk.*
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class VideoViewModelTest {

    private lateinit var viewModel: VideoViewModel
    private lateinit var mockPlayer: ExoPlayer

    @Before
    fun setUp() {
        viewModel = VideoViewModel()
        mockPlayer = mockk(relaxed = true)
    }

    @Test
    fun `restorePlayerState should restore state to ExoPlayer`() {
        // Arrange
        every { mockPlayer.playWhenReady } returns true
        every { mockPlayer.currentPosition } returns 5000L
        viewModel.savePlayerState(mockPlayer)

        // Act
        viewModel.restorePlayerState(mockPlayer)

        // Assert
        verify { mockPlayer.playWhenReady = true }
        verify { mockPlayer.seekTo(5000L) }
    }

    @Test
    fun `savePlayerState should save state from ExoPlayer`() {
        // Arrange
        every { mockPlayer.currentPosition } returns 3000L
        every { mockPlayer.playWhenReady } returns false

        // Act
        viewModel.savePlayerState(mockPlayer)

        // Assert
        assertEquals(3000L, viewModel.currentPosition)
        assertEquals(false, viewModel.playWhenReady)
    }

    @Test
    fun `restorePlayerState should apply saved state to ExoPlayer`() {
        // Arrange
        every { mockPlayer.currentPosition } returns 3000L
        every { mockPlayer.playWhenReady } returns false
        viewModel.savePlayerState(mockPlayer)

        // Change the state to different values to test restore
        every { mockPlayer.currentPosition } returns 1000L
        every { mockPlayer.playWhenReady } returns true

        // Act
        viewModel.restorePlayerState(mockPlayer)

        // Assert
        verify { mockPlayer.playWhenReady = false }
        verify { mockPlayer.seekTo(3000L) }
    }
}
