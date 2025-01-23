package com.mobile.animauxdomestiques.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

fun customTypography(fontSize: Float): Typography {
    return Typography(
        displayLarge = TextStyle(fontSize = fontSize.sp),
        displayMedium = TextStyle(fontSize = (fontSize * 0.9f).sp),
        displaySmall = TextStyle(fontSize = (fontSize * 0.8f).sp),
        headlineLarge = TextStyle(fontSize = fontSize.sp),
        headlineMedium = TextStyle(fontSize = (fontSize * 0.9f).sp),
        headlineSmall = TextStyle(fontSize = (fontSize * 0.8f).sp),
        titleLarge = TextStyle(fontSize = fontSize.sp),
        titleMedium = TextStyle(fontSize = (fontSize * 0.9f).sp),
        titleSmall = TextStyle(fontSize = (fontSize * 0.8f).sp),
        bodyLarge = TextStyle(fontSize = fontSize.sp),
        bodyMedium = TextStyle(fontSize = (fontSize * 0.9f).sp),
        bodySmall = TextStyle(fontSize = (fontSize * 0.8f).sp),
        labelLarge = TextStyle(fontSize = fontSize.sp),
        labelMedium = TextStyle(fontSize = (fontSize * 0.9f).sp),
        labelSmall = TextStyle(fontSize = (fontSize * 0.8f).sp)
    )
}
