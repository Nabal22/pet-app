package com.mobile.animauxdomestiques.ui.theme.components

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun customButtonsColor(): ButtonColors {
    return ButtonColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary,
        disabledContainerColor = MaterialTheme.colorScheme.outline,
        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}