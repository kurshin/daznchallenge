package com.kurshin.daznchallenge.domain.model

import io.mockk.*
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.test.assertEquals

class DaznEventTest {

    @Test
    fun `test sortByDate`() {
        val event1 = DaznEvent(
            date = "2024-06-16T10:00:00.000Z",
            id = "1",
            imageUrl = "url1",
            subtitle = "subtitle1",
            title = "title1",
            videoUrl = "videoUrl1"
        )
        val event2 = DaznEvent(
            date = "2024-06-15T10:00:00.000Z",
            id = "2",
            imageUrl = "url2",
            subtitle = "subtitle2",
            title = "title2",
            videoUrl = "videoUrl2"
        )

        val events = listOf(event1, event2)
        val sortedEvents = events.sortByDate()

        assertEquals(listOf(event2, event1), sortedEvents)
    }

    @Test
    fun `test toDaznDate - Today`() {
        mockkStatic(LocalDateTime::class)
        val dateTime = LocalDateTime.of(2024, 6, 16, 10, 0, 0)
        every { LocalDateTime.now(any<ZoneId>()) } returns dateTime

        val dateStr = "2024-06-16T10:00:00.000Z"
        val result = dateStr.toDaznDate()

        assertEquals("Today, 10:00", result)
        unmockkStatic(LocalDateTime::class)
    }

    @Test
    fun `test toDaznDate - Yesterday`() {
        mockkStatic(LocalDateTime::class)
        val dateTime = LocalDateTime.of(2024, 6, 16, 10, 0, 0)
        every { LocalDateTime.now(any<ZoneId>()) } returns dateTime

        val dateStr = "2024-06-15T10:00:00.000Z"
        val result = dateStr.toDaznDate()

        assertEquals("Yesterday, 10:00", result)
        unmockkStatic(LocalDateTime::class)
    }

    @Test
    fun `test toDaznDate - Tomorrow`() {
        mockkStatic(LocalDateTime::class)
        val dateTime = LocalDateTime.of(2024, 6, 16, 10, 0, 0)
        every { LocalDateTime.now(any<ZoneId>()) } returns dateTime

        val dateStr = "2024-06-17T10:00:00.000Z"
        val result = dateStr.toDaznDate()

        assertEquals("Tomorrow, 10:00", result)
        unmockkStatic(LocalDateTime::class)
    }

    @Test
    fun `test toDaznDate - In X days`() {
        mockkStatic(LocalDateTime::class)
        val dateTime = LocalDateTime.of(2024, 6, 16, 10, 0, 0)
        every { LocalDateTime.now(any<ZoneId>()) } returns dateTime

        val dateStr = "2024-06-18T10:00:00.000Z"
        val result = dateStr.toDaznDate()

        assertEquals("In 2 days, 10:00", result)
        unmockkStatic(LocalDateTime::class)
    }

    @Test
    fun `test toDaznDate - Invalid Date`() {
        mockkStatic(LocalDateTime::class)
        val dateTime = LocalDateTime.of(2024, 6, 16, 10, 0, 0)
        every { LocalDateTime.now(any<ZoneId>()) } returns dateTime

        val dateStr = "2024-06-14T10:00:00.000Z"
        val result = dateStr.toDaznDate()

        assertEquals("Invalid Date", result)
        unmockkStatic(LocalDateTime::class)
    }
}
