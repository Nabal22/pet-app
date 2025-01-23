package com.mobile.animauxdomestiques.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Specie(
    @PrimaryKey(autoGenerate = true) val specieId: Int = 0,
    val name: String
)
