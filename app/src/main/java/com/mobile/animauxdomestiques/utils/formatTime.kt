package com.mobile.animauxdomestiques.utils

import com.mobile.animauxdomestiques.model.enums.Day
import com.mobile.animauxdomestiques.model.enums.ReminderType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun formatTime(time: Long, reminderType: ReminderType): String {
    val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault())

    val dayOfWeek = dateTime.dayOfWeek

    val selectedDay = Day.entries.first { it.associatedJavaTimeValue == dayOfWeek }

    return when (reminderType) {
        ReminderType.DAILY -> dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
        ReminderType.WEEKLY -> "$selectedDay à ${dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
        ReminderType.UNIQUE -> dateTime.format(DateTimeFormatter.ofPattern("dd MMMM yyyy à HH:mm"))
    }
}
