package com.kurshin.daznchallenge.presentation.ui.schedule

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.kurshin.daznchallenge.domain.model.DaznEvent
import com.kurshin.daznchallenge.domain.repository.IEventRepository
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.*
import org.junit.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class ScheduleViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var repository: IEventRepository
    private lateinit var viewModel: ScheduleViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = ScheduleViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cancel()
        unmockkAll()
    }

    @Test
    fun `fetchScheduleEvents should update events LiveData`() = testScope.runTest {
        val mockEvents = listOf(
            DaznEvent("2024-06-16T10:00:00.000Z", "1", "url1", "subtitle1", "title1", "videoUrl1"),
            DaznEvent("2024-06-15T10:00:00.000Z", "2", "url2", "subtitle2", "title2", "videoUrl2")
        )

        coEvery { repository.getSchedule() } returns mockEvents

        val observer = mockk<Observer<List<DaznEvent>>>(relaxed = true)
        viewModel.events.observeForever(observer)

        val job = viewModel.fetchScheduleEvents()

        // Advance the test dispatcher to allow the coroutine to execute
        advanceTimeBy(DELAY_TIME_MILLIS)
        job.cancelAndJoin()

        verify { observer.onChanged(mockEvents) }
        viewModel.events.removeObserver(observer)
    }

    @Test
    fun `fetchScheduleEvents should emit error message on exception`() = testScope.runTest {
        val errorMessage = "Network error"
        coEvery { repository.getSchedule() } throws RuntimeException(errorMessage)

        val errorCollector = mutableListOf<String>()
        val job = launch { viewModel.error.toList(errorCollector) }

        val fetchJob = viewModel.fetchScheduleEvents()

        // Advance the test dispatcher to allow the coroutine to execute
        advanceTimeBy(DELAY_TIME_MILLIS)
        fetchJob.cancelAndJoin()

        assertTrue(errorCollector.isNotEmpty(), "Expected non-empty error collector")
        assertEquals(errorMessage, errorCollector[0])

        job.cancel()
    }
}

