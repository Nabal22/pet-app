package com.mobile.animauxdomestiques.data.entities.activity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["activityId"],
    foreignKeys = [ForeignKey(
        entity = Activity::class,
        parentColumns = ["activityId"],
        childColumns = ["activityId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class GlobalActivity(
    val activityId: Int
)
