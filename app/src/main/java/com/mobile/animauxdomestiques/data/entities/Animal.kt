package com.mobile.animauxdomestiques.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mobile.animauxdomestiques.model.enums.Gender

@Entity(
    foreignKeys = [ForeignKey(
        entity = Specie::class,
        parentColumns = ["specieId"],
        childColumns = ["specieId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["name", "specieId"], unique = true)]
)
data class Animal(
    @PrimaryKey(autoGenerate = true) val animalId: Int = 0,
    val name: String,
    val breed: String? = "",
    val gender: Gender? = Gender.UNSPECIFIED,
    val age: Int? = null,
    val weight: Float? = null,
    val imagePath: String? = null,
    val specieId: Int
)