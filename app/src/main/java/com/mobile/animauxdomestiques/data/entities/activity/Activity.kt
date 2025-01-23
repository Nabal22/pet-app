package com.mobile.animauxdomestiques.data.entities.activity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Activity(
    @PrimaryKey(autoGenerate = true) val activityId: Int = 0,
    val name: String,
    val description: String?
)
