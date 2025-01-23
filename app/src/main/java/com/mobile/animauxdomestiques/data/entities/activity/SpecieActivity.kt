package com.mobile.animauxdomestiques.data.entities.activity

import androidx.room.Entity
import androidx.room.ForeignKey
import com.mobile.animauxdomestiques.data.entities.Specie

@Entity(
    primaryKeys = ["activityId", "specieId"],
    foreignKeys = [
        ForeignKey(
            entity = Activity::class,
            parentColumns = ["activityId"],
            childColumns = ["activityId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Specie::class,
            parentColumns = ["specieId"],
            childColumns = ["specieId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SpecieActivity(
    val activityId: Int,
    val specieId: Int
)
