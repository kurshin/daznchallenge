package com.kurshin.daznchallenge.domain.model

import android.annotation.SuppressLint
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class DaznEvent(
    val date: String,
    val id: String,
    val imageUrl: String,
    val subtitle: String,
    val title: String,
    val videoUrl: String?
)

fun List<DaznEvent>.sortByDate(): List<DaznEvent> {
    val dateFormatter = DateTimeFormatter.ofPattern(DAZN_TIME_PATTERN)
    return this.sortedBy { event ->
        LocalDateTime.parse(event.date, dateFormatter)
    }
}

@SuppressLint("NewApi")
fun String.toDaznDate(): String {
    val formatter = DateTimeFormatter.ofPattern(DAZN_TIME_PATTERN)
    val dateTime = LocalDateTime.parse(this, formatter)

    val now = LocalDateTime.now(ZoneId.of("UTC"))
    val today = now.toLocalDate()
    val inputDate = dateTime.toLocalDate()

    val daysBetween = ChronoUnit.DAYS.between(today, inputDate)

    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val formattedTime = dateTime.format(timeFormatter)

    return when {
        daysBetween == -1L -> "Yesterday, $formattedTime"
        daysBetween == 0L -> "Today, $formattedTime"
        daysBetween == 1L -> "Tomorrow, $formattedTime"
        daysBetween > 1L -> "In $daysBetween days, $formattedTime"
        else -> "Invalid Date"
    }
}

const val DAZN_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"