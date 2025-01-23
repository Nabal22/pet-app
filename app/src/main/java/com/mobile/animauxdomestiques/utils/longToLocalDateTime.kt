package com.mobile.animauxdomestiques.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun longToLocalDateTime(time: Long?): LocalDateTime {
    return time?.let {
        LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
    } ?: LocalDateTime.now()
}
