package com.mobile.animauxdomestiques.model

import androidx.room.Embedded
import androidx.room.Relation
import com.mobile.animauxdomestiques.data.entities.Animal
import com.mobile.animauxdomestiques.data.entities.activity.ActivityConfiguration

data class ActivityConfigurationWithAnimal(
    @Embedded val activityConfiguration: ActivityConfiguration,
    @Relation(
        parentColumn = "animalId",
        entityColumn = "animalId"
    ) val animal: Animal
)
