package com.mobile.animauxdomestiques.data.entities.activity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mobile.animauxdomestiques.data.entities.Animal
import com.mobile.animauxdomestiques.model.enums.ReminderType

@Entity(
    foreignKeys = [ForeignKey(
        entity = Activity::class,
        parentColumns = ["activityId"],
        childColumns = ["activityId"],
        onDelete = ForeignKey.CASCADE
    ),ForeignKey(
        entity = Animal::class,
        parentColumns = ["animalId"],
        childColumns = ["animalId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["animalId", "activityId"], unique = true)]
)
data class ActivityConfiguration(
    @PrimaryKey(autoGenerate = true) val activityConfigurationId: Int = 0,
    val animalId: Int,
    val activityId: Int,
    val time: Long? = null,
    val reminderType: ReminderType? = null,
    val isConfigured: Boolean = false
)
