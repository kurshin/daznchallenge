package com.kurshin.daznchallenge.presentation.ui.events

import com.kurshin.daznchallenge.domain.model.DaznEvent
import com.kurshin.daznchallenge.domain.repository.IEventRepository
import com.kurshin.daznchallenge.presentation.ui.events.EventsViewModel
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.*
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class EventsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: IEventRepository
    private lateinit var viewModel: EventsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = EventsViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `fetchEvents should update events state flow`() = runTest {
        val mockEvents = listOf(
            DaznEvent("2024-06-16T10:00:00.000Z", "1", "url1", "subtitle1", "title1", "videoUrl1"),
            DaznEvent("2024-06-15T10:00:00.000Z", "2", "url2", "subtitle2", "title2", "videoUrl2")
        )

        coEvery { repository.getAllEvents() } returns flowOf(mockEvents)

        viewModel.fetchEvents()
        advanceUntilIdle()  // Ensure all coroutines complete

        assertEquals(mockEvents, viewModel.events.value)
    }

    @Test
    fun `init should call fetchEvents`() = runTest {
        val mockEvents = listOf(
            DaznEvent("2024-06-16T10:00:00.000Z", "1", "url1", "subtitle1", "title1", "videoUrl1")
        )

        coEvery { repository.getAllEvents() } returns flowOf(mockEvents)
        viewModel = EventsViewModel(repository)
        advanceUntilIdle()  // Ensure all coroutines complete

        assertEquals(mockEvents, viewModel.events.value)
    }
}
