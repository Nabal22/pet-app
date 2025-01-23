package com.mobile.animauxdomestiques.model.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarViewDay
import androidx.compose.material.icons.filled.CalendarViewWeek
import androidx.compose.material.icons.filled.Event
import androidx.compose.ui.graphics.vector.ImageVector

enum class ReminderType(private val displayName: String, private val icon: ImageVector) {
    DAILY ("Quotidien", Icons.Default.CalendarViewDay),
    WEEKLY("Hebdomadaire", Icons.Default.CalendarViewWeek),
    UNIQUE("Ponctuel", Icons.Default.Event);

    override fun toString(): String {
        return displayName
    }

    fun toIcon(): ImageVector{
        return icon
    }
}