package com.kurshin.daznchallenge.data

import com.kurshin.daznchallenge.data.remote.DaznService
import com.kurshin.daznchallenge.domain.model.DaznEvent
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class EventRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var apiService: DaznService
    private lateinit var eventRepository: EventRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        apiService = mockk()
        eventRepository = EventRepository(apiService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
        unmockkAll()
    }

    @Test
    fun `getAllEvents should emit list of DaznEvent`() = runBlocking {
        val mockEvents = listOf(
            DaznEvent("2024-06-16T10:00:00.000Z", "1", "url1", "subtitle1", "title1", "videoUrl1"),
            DaznEvent("2024-06-15T10:00:00.000Z", "2", "url2", "subtitle2", "title2", "videoUrl2")
        )

        coEvery { apiService.fetchAllEvents() } returns mockEvents

        val result = eventRepository.getAllEvents().first()

        assertEquals(mockEvents, result)
        coVerify { apiService.fetchAllEvents() }
    }

    @Test
    fun `getSchedule should return list of DaznEvent`() = runBlocking {
        val mockSchedule = listOf(
            DaznEvent("2024-06-16T10:00:00.000Z", "1", "url1", "subtitle1", "title1", "videoUrl1"),
            DaznEvent("2024-06-15T10:00:00.000Z", "2", "url2", "subtitle2", "title2", "videoUrl2")
        )

        coEvery { apiService.fetchSchedule() } returns mockSchedule

        val result = eventRepository.getSchedule()

        assertEquals(mockSchedule, result)
        coVerify { apiService.fetchSchedule() }
    }
}
