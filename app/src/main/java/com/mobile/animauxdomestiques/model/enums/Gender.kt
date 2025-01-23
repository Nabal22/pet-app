package com.mobile.animauxdomestiques.model.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.ui.graphics.vector.ImageVector

enum class Gender(private val displayName: String, private val icon: ImageVector) {
    MALE("Mâle", Icons.Default.Male),
    FEMALE("Femelle", Icons.Default.Female),
    UNSPECIFIED("Inconnu", Icons.Default.QuestionMark);

    override fun toString(): String {
        return displayName
    }

    fun toIcon(): ImageVector {
        return icon
    }
}
